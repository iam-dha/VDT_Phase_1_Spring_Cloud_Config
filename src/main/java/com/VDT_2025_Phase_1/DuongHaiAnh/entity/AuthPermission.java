package com.VDT_2025_Phase_1.DuongHaiAnh.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "auth_permission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "rolePermissions")
public class AuthPermission {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @OneToMany(mappedBy = "permission",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuthRolePermission> rolePermissions = new ArrayList<>();

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private ZonedDateTime createdAt = ZonedDateTime.now();
}
