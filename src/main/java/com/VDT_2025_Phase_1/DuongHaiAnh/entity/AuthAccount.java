package com.VDT_2025_Phase_1.DuongHaiAnh.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.UuidGenerator;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "auth_account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"sessions", "userInformation, authPasswordReset"})
public class AuthAccount {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "account", nullable = false, unique = true, length = 50)
    private String account;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt = ZonedDateTime.now();

    // MAPPING

    @JsonIgnore
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private UserInformation userInformation;

    @JsonIgnore
    @OneToOne(mappedBy = "account", cascade =  CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private AuthPasswordReset authPasswordReset;

    @JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AuthSession> sessions = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuthAccountRole> accountRoles = new ArrayList<>();



    @Transient
    public List<AuthRole> getRoles() {
        return accountRoles.stream().map(AuthAccountRole::getRole).collect(Collectors.toList());
    }
}
