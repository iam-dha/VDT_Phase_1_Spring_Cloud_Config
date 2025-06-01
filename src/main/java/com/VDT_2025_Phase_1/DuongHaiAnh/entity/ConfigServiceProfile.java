package com.VDT_2025_Phase_1.DuongHaiAnh.entity;


import com.VDT_2025_Phase_1.DuongHaiAnh.entity.SerializableKey.ConfigServiceProfileId;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "config_service_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigServiceProfile {
    @EmbeddedId
    private ConfigServiceProfileId id;

    @Column(name = "added_at", nullable = false)
    private ZonedDateTime addedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", insertable = false, updatable = false)
    private ConfigService service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ConfigProfile profile;

    @PrePersist
    public void prePersist() {
        this.addedAt = ZonedDateTime.now();
    }

}
