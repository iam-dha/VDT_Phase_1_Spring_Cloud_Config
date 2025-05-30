package com.VDT_2025_Phase_1.DuongHaiAnh.entity;

import com.VDT_2025_Phase_1.DuongHaiAnh.entity.SerializableKey.AuthAccountRoleId;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "auth_account_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthAccountRole {
    @EmbeddedId
    private AuthAccountRoleId id;

    @ManyToOne
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    private AuthAccount account;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private AuthRole role;

    @Column(name = "assigned_at", nullable = false)
    private ZonedDateTime assignedAt;
}
