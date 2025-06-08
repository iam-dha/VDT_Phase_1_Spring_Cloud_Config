package com.VDT_2025_Phase_1.DuongHaiAnh.controller.client;

import com.VDT_2025_Phase_1.DuongHaiAnh.dto.AuthenticatedUserDTO;
import com.VDT_2025_Phase_1.DuongHaiAnh.dto.AuthorizationDTO;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.AuthAccount;
import com.VDT_2025_Phase_1.DuongHaiAnh.payload.request.AuthRefreshRequest;
import com.VDT_2025_Phase_1.DuongHaiAnh.payload.request.AuthRegisterInfoRequest;
import com.VDT_2025_Phase_1.DuongHaiAnh.payload.request.AuthVerifyOtpRequest;
import com.VDT_2025_Phase_1.DuongHaiAnh.service.AuthService;
import com.VDT_2025_Phase_1.DuongHaiAnh.service.AuthSessionService;
import com.VDT_2025_Phase_1.DuongHaiAnh.service.UserService;
import com.VDT_2025_Phase_1.DuongHaiAnh.utils.CodeGenerationUtilsHelper;
import com.VDT_2025_Phase_1.DuongHaiAnh.utils.JWTUtilsHelper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.VDT_2025_Phase_1.DuongHaiAnh.payload.request.AuthSignUpRequest;
import com.VDT_2025_Phase_1.DuongHaiAnh.payload.response.ResponseData;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

//    @Autowired
//            @Qualifier("Bean_Name") => 2 class have same name
//    AuthAccountInterface authAccountInterface;

    @Autowired
    AuthService authService;

    @Autowired
    AuthSessionService authSessionService;

    @Autowired
    JWTUtilsHelper jwtUtilsHelper;

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestParam String account, @RequestParam String password, HttpServletRequest request){
        ResponseData responseData = new ResponseData();
        try{
            AuthAccount user = authService.checkLogin(account, password);
            if(user == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseData.error(401, "Invalid login credentials"));
            }
            AuthorizationDTO authorizationDTO = authService.saveNewSession(user, request);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseData.success("Login Success full", authorizationDTO));
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(responseData,HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/registerOtp")
    public ResponseEntity<?> registerOtp(@RequestBody AuthSignUpRequest authSignUpRequest){
        String account = authSignUpRequest.getAccount();
        String email = authSignUpRequest.getEmail();
        authService.checRegisterRequest(authSignUpRequest);
        String otp = CodeGenerationUtilsHelper.generateOtp();
        authService.sendOtp(email, account, otp);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseData.success("Register Successfully, please check your email", ""));
    }

    @PostMapping("/verOtp")
    public ResponseEntity<?> verOtp(@RequestBody AuthVerifyOtpRequest authVerifyOtpRequest){
        String account = authVerifyOtpRequest.getAccount();
        String otp = authVerifyOtpRequest.getOtp();
        authService.verifyOtp(account, otp);
        String token = CodeGenerationUtilsHelper.generate32CharHexToken();
        authService.saveToken(account, token);
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("token", token);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseData.success("Verify Otp Successfully", responseData));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRegisterInfoRequest authRegisterInfoRequest, HttpServletRequest request){
        AuthorizationDTO authorizationDTO= authService.createUser(authRegisterInfoRequest, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseData.success("Register Successfully", authorizationDTO));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody AuthRefreshRequest requestBody, HttpServletRequest request){
        AuthorizationDTO authorizationDTO = authService.refreshSession(requestBody.getRefreshToken(), request);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseData.success("Refresh Successfully", authorizationDTO));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody AuthRefreshRequest requestBody){
        String refreshToken = requestBody.getRefreshToken();
        authService.logOut(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseData.success("Log out Successfully", refreshToken));
    }

    @PostMapping("/test")
    public ResponseEntity<?> test(@RequestBody String name){
        System.out.println(name);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseData.success("Test Successfully", name));
    }
}
