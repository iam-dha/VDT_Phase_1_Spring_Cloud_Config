package com.VDT_2025_Phase_1.DuongHaiAnh.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AuthRegisterInfoRequest {
    private String account;
    private String email;
    private String token;
    private String firstName;
    private String lastName;
    private String password;
    private String avatarUrl = "https://images.spiderum.com/sp-images/f2e904a0206011edbf94ab4c5b305113.jpeg";
    private String phoneNumber;
    private LocalDate birthDate;
}
