package com.VDT_2025_Phase_1.DuongHaiAnh.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthVerifyOtpRequest {
    private String account;
    private String otp;
}
