package com.VDT_2025_Phase_1.DuongHaiAnh.service.imp;

import com.VDT_2025_Phase_1.DuongHaiAnh.dto.AccountDTO;
import com.VDT_2025_Phase_1.DuongHaiAnh.dto.AuthenticatedUserDTO;
import com.VDT_2025_Phase_1.DuongHaiAnh.dto.AuthorizationDTO;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.*;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.SerializableKey.AuthAccountRoleId;
import com.VDT_2025_Phase_1.DuongHaiAnh.payload.request.AuthRegisterInfoRequest;
import com.VDT_2025_Phase_1.DuongHaiAnh.repository.AuthAccountRoleRepository;
import com.VDT_2025_Phase_1.DuongHaiAnh.repository.AuthRoleRepository;
import com.VDT_2025_Phase_1.DuongHaiAnh.repository.UserInformationRepository;
import com.VDT_2025_Phase_1.DuongHaiAnh.service.AuthService;
import com.VDT_2025_Phase_1.DuongHaiAnh.service.AuthSessionService;
import com.VDT_2025_Phase_1.DuongHaiAnh.service.CachingService;
import com.VDT_2025_Phase_1.DuongHaiAnh.service.MailerService;
import com.VDT_2025_Phase_1.DuongHaiAnh.setting.SystemSetting;
import com.VDT_2025_Phase_1.DuongHaiAnh.utils.ExpirationUtilsHelper;
import com.VDT_2025_Phase_1.DuongHaiAnh.utils.JWTUtilsHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import com.VDT_2025_Phase_1.DuongHaiAnh.repository.AuthAccountRepository;
import com.VDT_2025_Phase_1.DuongHaiAnh.payload.request.AuthSignUpRequest;

@Slf4j
@Service
public class AuthServiceImp implements AuthService {

    public static final String DEFAULT_ROLE = "USER";

    @Autowired
    private AuthSessionService authSessionService;

    @Autowired
    private MailerService mailerService;

    @Autowired
    private CachingService cachingService;

    @Autowired
    private AuthAccountRepository authAccountRepository;

    @Autowired
    private AuthAccountRoleRepository authAccountRoleRepository;

    @Autowired
    private AuthRoleRepository authRoleRepository;

    @Autowired
    private UserInformationRepository userInformationRepository;

    @Autowired
    private JWTUtilsHelper jwtUtilsHelper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private AuthenticatedUserDTO parseAccountDTO(AuthAccount user){

        return AuthenticatedUserDTO.builder()
                .account(user.getAccount())
                .userId(user.getId())
                .roles(user.getRoles().stream().map(authRole -> authRole.getName()).collect(Collectors.toList()))
                .permissions(user.getRoles().stream().flatMap(role -> role.getPermissions().stream()).map(AuthPermission::getName).distinct().collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<AccountDTO> getAllAccount(){
        List<AuthAccount> accountList = authAccountRepository.findAll();
        List<AccountDTO>  accountDTOList = new ArrayList<>();
        for(AuthAccount account : accountList){
            AccountDTO accountDTO = AccountDTO.builder()
                    .account(account.getAccount())
                    .email(account.getEmail())
                    .password("hidden")
                    .isActive(account.getIsActive())
                    .build();
            accountDTOList.add(accountDTO);
        }
        return accountDTOList;
    }

    @Override
    public AuthAccount checkLogin(String account, String password){
        AuthAccount user = authAccountRepository.findByAccount(account);
        if (user == null || !passwordEncoder.matches(password, user.getPassword()) || !user.getIsActive()) {
            return null;
        }
        return user;
    }


    @Override
    public AuthorizationDTO saveNewSession(AuthAccount user, HttpServletRequest request) {
        AuthenticatedUserDTO authenticatedUserDTO = parseAccountDTO(user);
        String accessToken = jwtUtilsHelper.generateAccessToken(authenticatedUserDTO);
        String refreshToken = jwtUtilsHelper.generateRefreshToken(authenticatedUserDTO);
        ZonedDateTime refreshTokenExpiration = ExpirationUtilsHelper.calculateExpirationZoned(jwtUtilsHelper.getJwtRefreshTokenExpiration(), ZonedDateTime.now(ZoneId.systemDefault()));
        AuthorizationDTO authorizationDTO = AuthorizationDTO.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .roles(authenticatedUserDTO.getRoles())
                .build();
        authSessionService.saveNewSession(user, refreshToken, request, refreshTokenExpiration);
        return authorizationDTO;
    }

    @Override
    public AuthorizationDTO refreshSession(String refreshToken, HttpServletRequest request) {
        try{
            String extractedAccount = jwtUtilsHelper.extractSubject(refreshToken, false); // get account
            if(extractedAccount == null){
                throw new NoSuchElementException("Account not found");
            }
            AuthAccount account = authAccountRepository.findByAccount(extractedAccount);
            if(account == null){
                throw new NoSuchElementException("Account not found");
            }
            AuthenticatedUserDTO authenticatedUserDTO = parseAccountDTO(account);
            return authSessionService.refreshSession(authenticatedUserDTO, refreshToken, request);
        }catch (Exception e){
            throw e;
        }

    }

    @Override
    public Boolean logOut(String refreshToken) {
        return authSessionService.logOut(refreshToken);
    }

    @Override
    public boolean checRegisterRequest(AuthSignUpRequest authSignUpRequest) {
        String email = authSignUpRequest.getEmail();
        String account = authSignUpRequest.getAccount();
        if (email == null || account == null || email.isBlank() || account.isBlank()) {
            throw new IllegalArgumentException("Email and account are required");
        }
        boolean existsUser = authAccountRepository.existsByAccountOrEmail(email,  account);
        if(existsUser){
            throw new IllegalStateException("Account or email already exists");
        }
        return true;
    }

    @Override
    public void sendOtp(String email, String account,  String otp){
        long otpExpiration = SystemSetting.OTPEXPIRATION;
        long otpSendingLimit = SystemSetting.OTPSENDINGLIMIT;

        String resendKey = "otp:limit:%s".formatted(account);
        if(cachingService.getKey(resendKey) != null){
            throw new IllegalStateException("Please wait before requesting a new OTP.");
        }

        String otpKey = "otp:code:%s".formatted(account);
        cachingService.saveKey(otpKey, otp, otpExpiration, TimeUnit.MINUTES);
        cachingService.saveKey(resendKey, "1", otpSendingLimit, TimeUnit.MINUTES);

        mailerService.sendOtp(email, otp);
        log.info("Register successfully, OTP sent to email: {}", email);
    }

    @Override
    public boolean verifyOtp(String account, String otp){
        String otpKey = "otp:code:%s".formatted(account);
        String savedOtp = cachingService.getKey(otpKey);
        if(savedOtp == null || !savedOtp.equals(otp)){
            throw new IllegalStateException("OTP invalid or expired.");
        }
        return true;
    }

    @Override
    public boolean saveToken(String account, String token) {
        String tokenKey = "otp:verified:%s".formatted(account);
        cachingService.deleteKey(tokenKey);
        cachingService.saveKey(tokenKey, token, SystemSetting.TOKENEXPIRATION, TimeUnit.MINUTES);
        return true;
    }

    @Override
    @Transactional
    public AuthorizationDTO createUser(AuthRegisterInfoRequest authRegisterInfoRequest, HttpServletRequest request){
        String token = authRegisterInfoRequest.getToken();
        String account = authRegisterInfoRequest.getAccount();
        String tokenKey = "otp:verified:%s".formatted(account);
        String savedToken = cachingService.getKey(tokenKey);
        if(token == null || token.isBlank() || savedToken == null || savedToken.isBlank() || !savedToken.equals(token) ){
            throw new IllegalStateException("Token is invalid or expired.");
        }

        String email = authRegisterInfoRequest.getEmail();
        String password = authRegisterInfoRequest.getPassword();
        if(email == null || email.isBlank() || password == null || password.isBlank()){
            throw new IllegalArgumentException("Email and password are required");
        }
        String hashedPassword = passwordEncoder.encode(password);
        AuthAccount authAccount = AuthAccount.builder()
                .account(account)
                .email(email)
                .password(hashedPassword)
                .createdAt(ZonedDateTime.now())
                .isActive(true)
                .build();

        AuthAccount savedAccount = authAccountRepository.save(authAccount);
        String otpKey = "otp:code:%s".formatted(account);
        String resendKey = "otp:limit:%s".formatted(account);

        cachingService.deleteKey(tokenKey);
        cachingService.deleteKey(otpKey);
        cachingService.deleteKey(resendKey);
        AuthRole userRole = authRoleRepository.findByName(DEFAULT_ROLE).orElseThrow(() -> new IllegalStateException("Default role not found"));;
        AuthAccountRoleId accountRoleId = new AuthAccountRoleId();
        accountRoleId.setAccountId(savedAccount.getId());
        accountRoleId.setRoleId(userRole.getId());
        AuthAccountRole accountRole = AuthAccountRole.builder()
                .id(accountRoleId)
                .assignedAt(ZonedDateTime.now())
                .account(savedAccount)
                .role(userRole)
                .build();

        authAccountRoleRepository.save(accountRole);
        String avatarUrl = authRegisterInfoRequest.getAvatarUrl();
        String firstName = (authRegisterInfoRequest.getFirstName() != null && !authRegisterInfoRequest.getFirstName().isBlank()) ? authRegisterInfoRequest.getFirstName() : "Unknown";
        String lastName = (authRegisterInfoRequest.getLastName() != null && !authRegisterInfoRequest.getLastName().isBlank()) ? authRegisterInfoRequest.getLastName() : "User";
        LocalDate birthDate = authRegisterInfoRequest.getBirthDate() != null ? authRegisterInfoRequest.getBirthDate() : LocalDate.of(2004, 7, 6);
        String phoneNumber = (authRegisterInfoRequest.getPhoneNumber() != null && !authRegisterInfoRequest.getPhoneNumber().isBlank()) ? authRegisterInfoRequest.getPhoneNumber() : "0357624005";
        if (avatarUrl == null || avatarUrl.isBlank()) {
            avatarUrl = "https://images.spiderum.com/sp-images/f2e904a0206011edbf94ab4c5b305113.jpeg";
        }
        UserInformation userInformation = UserInformation.builder()
                .firstName(firstName)
                .lastName(lastName)
                .birthDate(birthDate)
                .phoneNumber(phoneNumber)
                .avatarUrl(avatarUrl)
                .account(authAccount)
                .createdAt(ZonedDateTime.now())
                .build();

        userInformationRepository.save(userInformation);
        log.info("User {} registered successfully with email {}", account, email);
        return(saveNewSession(savedAccount,request));
    }

}
