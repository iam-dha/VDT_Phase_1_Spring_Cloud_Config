package com.VDT_2025_Phase_1.DuongHaiAnh.service;

import com.VDT_2025_Phase_1.DuongHaiAnh.dto.AccountDTO;
import com.VDT_2025_Phase_1.DuongHaiAnh.dto.AuthorizationDTO;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.AuthAccount;
import com.VDT_2025_Phase_1.DuongHaiAnh.payload.request.AuthRegisterInfoRequest;
import com.VDT_2025_Phase_1.DuongHaiAnh.payload.request.AuthSignUpRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface AuthService {
    List<AccountDTO> getAllAccount();
    AuthAccount checkLogin(String account, String password);
    AuthorizationDTO saveNewSession(AuthAccount user, HttpServletRequest request);
    AuthorizationDTO refreshSession(String refreshToken ,HttpServletRequest request);
    Boolean logOut(String refreshToken);
    boolean checRegisterRequest(AuthSignUpRequest authSignUpRequest);
    void sendOtp(String email, String account, String otp);
    boolean verifyOtp(String account, String otp);
    boolean saveToken(String account, String token);
    AuthorizationDTO createUser(AuthRegisterInfoRequest authRegisterInfoRequest, HttpServletRequest request);
}
