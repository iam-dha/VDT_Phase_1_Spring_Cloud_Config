package com.VDT_2025_Phase_1.DuongHaiAnh.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class AuthorizationDTO {
    private String accessToken;
    private String refreshToken;
    private List<String> roles = new ArrayList<>();
}
