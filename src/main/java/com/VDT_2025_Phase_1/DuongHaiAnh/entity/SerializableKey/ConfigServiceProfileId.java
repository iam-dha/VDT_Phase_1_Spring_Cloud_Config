package com.VDT_2025_Phase_1.DuongHaiAnh.entity.SerializableKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfigServiceProfileId {
    @Column(name = "service_id", nullable = false)
    private UUID serviceId;

    @Column(name = "profile_id", nullable = false)
    private UUID profileId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfigServiceProfileId)) return false;
        ConfigServiceProfileId that = (ConfigServiceProfileId) o;
        return Objects.equals(serviceId, that.serviceId) &&
                Objects.equals(profileId, that.profileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId, profileId);
    }

}
