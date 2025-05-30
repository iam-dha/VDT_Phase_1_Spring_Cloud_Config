package com.VDT_2025_Phase_1.DuongHaiAnh.repository;

import com.VDT_2025_Phase_1.DuongHaiAnh.entity.AuthAccount;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.AuthSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthSessionRepository extends JpaRepository<AuthSession, UUID> {
    List<AuthSession> findByAccountAndRevokedFalseAndExpiresAtAfterOrderByUpdatedAtAsc(AuthAccount account, ZonedDateTime now);
    Optional<AuthSession> findByRefreshTokenAndRevokedFalseAndExpiresAtAfter(String refreshToken, ZonedDateTime now);
    Optional<AuthSession> findByRefreshToken(String refreshToken);

}
