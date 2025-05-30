package com.VDT_2025_Phase_1.DuongHaiAnh.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class AuthenticatedUserDTO {
    private UUID userId;
    private String account;
    private List<String> roles;
    private List<String> permissions;

}
