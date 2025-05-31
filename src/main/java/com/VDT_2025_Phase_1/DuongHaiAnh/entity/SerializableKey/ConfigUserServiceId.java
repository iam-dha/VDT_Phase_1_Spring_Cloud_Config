package com.VDT_2025_Phase_1.DuongHaiAnh.entity.SerializableKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfigUserServiceId implements Serializable {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "service_id")
    private UUID serviceId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfigUserServiceId)) return false;
        ConfigUserServiceId that = (ConfigUserServiceId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(serviceId, that.serviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, serviceId);
    }

}
