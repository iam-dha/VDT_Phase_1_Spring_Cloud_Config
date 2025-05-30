package com.VDT_2025_Phase_1.DuongHaiAnh.service.imp;

import com.VDT_2025_Phase_1.DuongHaiAnh.dto.UserDetailDTO;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.AuthAccount;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.UserInformation;
import com.VDT_2025_Phase_1.DuongHaiAnh.repository.AuthAccountRepository;
import com.VDT_2025_Phase_1.DuongHaiAnh.repository.UserInformationRepository;
import com.VDT_2025_Phase_1.DuongHaiAnh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserInformationRepository userInformationRepository;

    @Autowired
    private AuthAccountRepository authAccountRepository;

    @Override
    public List<UserDetailDTO> getAllUser(){
        return userInformationRepository.findAll().stream()
                .map(userInfo -> UserDetailDTO.builder()
                        .email(userInfo.getAccount().getEmail())
                        .account(userInfo.getAccount().getAccount())
                        .firstName(userInfo.getFirstName())
                        .lastName(userInfo.getLastName())
                        .phoneNumber(userInfo.getPhoneNumber())
                        .birthDate(userInfo.getBirthDate())
                        .avatarUrl(userInfo.getAvatarUrl())
                        .createdAt(userInfo.getCreatedAt())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public UserDetailDTO getUserInfomation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("Invalid token");
        }
        String account = (String) authentication.getPrincipal();
        AuthAccount authAccount = authAccountRepository.findByAccount(account);
        if(authAccount == null || authAccount.getUserInformation() == null){
            throw new NoSuchElementException("User is not found");
        }
        UserInformation userInformation = authAccount.getUserInformation();
        return UserDetailDTO.builder()
                .email(authAccount.getEmail())
                .account(authAccount.getAccount())
                .firstName(userInformation.getFirstName())
                .lastName(userInformation.getLastName())
                .phoneNumber(userInformation.getPhoneNumber())
                .birthDate(userInformation.getBirthDate())
                .avatarUrl(userInformation.getAvatarUrl())
                .createdAt(userInformation.getCreatedAt())
                .build();
    }

    public UserDetailDTO deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("Invalid token");
        }
        String account = (String) authentication.getPrincipal();
        return null;
    }
}
