package com.VDT_2025_Phase_1.DuongHaiAnh.service;

import com.VDT_2025_Phase_1.DuongHaiAnh.dto.AuthenticatedUserDTO;
import com.VDT_2025_Phase_1.DuongHaiAnh.dto.AuthorizationDTO;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.AuthAccount;
import jakarta.servlet.http.HttpServletRequest;

import java.time.ZonedDateTime;

public interface AuthSessionService {
    public void saveNewSession(AuthAccount account, String refreshToken, HttpServletRequest request, ZonedDateTime expiresAt);
    public boolean checkRefreshToken(String refreshToken);
    public AuthorizationDTO refreshSession(AuthenticatedUserDTO authenticatedUserDTO, String refreshToken, HttpServletRequest request);
    public Boolean logOut(String refreshToken);
}
