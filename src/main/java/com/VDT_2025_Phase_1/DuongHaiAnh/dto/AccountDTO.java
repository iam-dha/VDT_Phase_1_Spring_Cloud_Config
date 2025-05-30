package com.VDT_2025_Phase_1.DuongHaiAnh.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountDTO {
    private String account;
    private String email;
    private String password;
    private Boolean isActive;
}
