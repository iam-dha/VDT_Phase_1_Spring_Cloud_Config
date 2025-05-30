package com.VDT_2025_Phase_1.DuongHaiAnh.service.imp;

import com.VDT_2025_Phase_1.DuongHaiAnh.dto.AuthenticatedUserDTO;
import com.VDT_2025_Phase_1.DuongHaiAnh.dto.AuthorizationDTO;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.AuthAccount;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.AuthSession;
import com.VDT_2025_Phase_1.DuongHaiAnh.repository.AuthSessionRepository;
import com.VDT_2025_Phase_1.DuongHaiAnh.service.AuthSessionService;
import com.VDT_2025_Phase_1.DuongHaiAnh.utils.ExpirationUtilsHelper;
import com.VDT_2025_Phase_1.DuongHaiAnh.utils.HashUtilsHelper;
import com.VDT_2025_Phase_1.DuongHaiAnh.utils.JWTUtilsHelper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuthSessionServiceImp implements AuthSessionService {

    private static final int MAX_SESSIONS = 5;

    @Autowired
    private AuthSessionRepository authSessionRepository;

    @Autowired
    private JWTUtilsHelper jwtUtilsHelper;

    private String extractIp(HttpServletRequest request){
        String headerIp = request.getHeader("X-Forwarded-For");
        if (headerIp != null && !headerIp.isEmpty()){
            return headerIp.split(",")[0];
        }
        return request.getRemoteAddr();
    }

    @Override
    public void saveNewSession(AuthAccount account, String refreshToken, HttpServletRequest request, ZonedDateTime expiresAt) {
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = extractIp(request);
        List<AuthSession> sessions = authSessionRepository.findByAccountAndRevokedFalseAndExpiresAtAfterOrderByUpdatedAtAsc(account, ZonedDateTime.now());
        if(sessions.size() >= MAX_SESSIONS){
            sessions.getFirst().setRevoked(true);
            authSessionRepository.save(sessions.getFirst());
        }
        String hashedRefreshToken = HashUtilsHelper.sha256(refreshToken);
        AuthSession newSession = AuthSession.builder()
                .account(account)
                .expiresAt(expiresAt)
                .refreshToken(hashedRefreshToken)
                .userAgent(userAgent)
                .ipAddress(ipAddress)
                .revoked(false)
                .updatedAt(ZonedDateTime.now())
                .createdAt(ZonedDateTime.now())
                .build();

        authSessionRepository.save(newSession);
    }

    @Override
    public boolean checkRefreshToken(String refreshToken) {
        try{
            String hashedToken = HashUtilsHelper.sha256(refreshToken);
            return authSessionRepository
                    .findByRefreshTokenAndRevokedFalseAndExpiresAtAfter(hashedToken, ZonedDateTime.now())
                    .isPresent();
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public AuthorizationDTO refreshSession(AuthenticatedUserDTO authenticatedUserDTO, String refreshToken, HttpServletRequest request) {
        String hashedRefreshToken = HashUtilsHelper.sha256(refreshToken);
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime newRefreshTokenExpiration = ExpirationUtilsHelper.calculateExpirationZoned(
                jwtUtilsHelper.getJwtRefreshTokenExpiration(), now);

        AuthSession authSession = authSessionRepository
                .findByRefreshTokenAndRevokedFalseAndExpiresAtAfter(hashedRefreshToken, now)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Refresh token kkk  is invalid or expired"));

        String newRefreshToken = jwtUtilsHelper.generateRefreshToken(authenticatedUserDTO);
        String hashedNewRefreshToken = HashUtilsHelper.sha256(newRefreshToken);

        authSession.setRefreshToken(hashedNewRefreshToken);
        authSession.setUpdatedAt(now);
        authSession.setExpiresAt(newRefreshTokenExpiration);
        authSession.setUserAgent(request.getHeader("User-Agent"));
        authSession.setIpAddress(extractIp(request));
        authSessionRepository.save(authSession);

        String newAccessToken = jwtUtilsHelper.generateAccessToken(authenticatedUserDTO);

        return AuthorizationDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .roles(authenticatedUserDTO.getRoles())
                .build();
    }

    @Override
    public Boolean logOut(String refreshToken) {
        Optional<AuthSession> sessionObj = authSessionRepository.findByRefreshToken(refreshToken);
        if(sessionObj.isPresent()){
            AuthSession session = sessionObj.get();
            session.setRevoked(true);
            session.setUpdatedAt(ZonedDateTime.now());
            authSessionRepository.save(session);
        }
        return true;
    }
}
