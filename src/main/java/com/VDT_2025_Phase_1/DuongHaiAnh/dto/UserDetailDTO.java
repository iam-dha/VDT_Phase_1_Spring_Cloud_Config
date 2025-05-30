package com.VDT_2025_Phase_1.DuongHaiAnh.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
public class UserDetailDTO {
    private String email;
    private String account;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthDate;
    private ZonedDateTime createdAt;
    private String avatarUrl;
}
