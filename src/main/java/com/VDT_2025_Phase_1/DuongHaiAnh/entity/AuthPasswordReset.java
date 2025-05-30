package com.VDT_2025_Phase_1.DuongHaiAnh.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "auth_password_reset")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "account")
public class AuthPasswordReset {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private AuthAccount account;

    @Column(name = "token",  nullable = false)
    private String token;

    @Column(name = "expires_at",  nullable = false)
    private ZonedDateTime expiresAt;

    @Column(name = "created_at",  nullable = false)
    private ZonedDateTime createdAt = ZonedDateTime.now();

}
