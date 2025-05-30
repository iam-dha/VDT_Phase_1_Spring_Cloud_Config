package com.VDT_2025_Phase_1.DuongHaiAnh.entity;

import com.VDT_2025_Phase_1.DuongHaiAnh.entity.SerializableKey.AuthRolePermissionId;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "auth_role_permission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRolePermission {
    @EmbeddedId
    private AuthRolePermissionId id;

    @Column(name = "granted_at", nullable = false)
    private ZonedDateTime grantedAt = ZonedDateTime.now();

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private AuthRole role;

    @ManyToOne
    @MapsId("permissionId")
    @JoinColumn(name = "permission_id")
    private AuthPermission permission;
}
