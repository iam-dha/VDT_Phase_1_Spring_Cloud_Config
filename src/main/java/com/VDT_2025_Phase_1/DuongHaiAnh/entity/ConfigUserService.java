package com.VDT_2025_Phase_1.DuongHaiAnh.entity;

import com.VDT_2025_Phase_1.DuongHaiAnh.entity.SerializableKey.ConfigUserServiceId;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "config_user_service")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigUserService {
    @EmbeddedId
    private ConfigUserServiceId id;

    @Column(name = "permissions", nullable = false)
    private String permissions;

    @Column(name = "granted_at", nullable = false)
    private ZonedDateTime grantedAt = ZonedDateTime.now();

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private AuthAccount account;

    @ManyToOne
    @MapsId("serviceId")
    @JoinColumn(name = "service_id", nullable = false)
    private ConfigService service;
}
