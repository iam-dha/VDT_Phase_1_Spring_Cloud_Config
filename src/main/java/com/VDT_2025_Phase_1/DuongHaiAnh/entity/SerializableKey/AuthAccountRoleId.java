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
public class AuthAccountRoleId {

    @Column(name = "account_id")
    private UUID accountId;

    @Column(name = "role_id")
    private UUID roleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthAccountRoleId)) return false;
        AuthAccountRoleId that = (AuthAccountRoleId) o;
        return Objects.equals(accountId, that.accountId) &&
                Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, roleId);
    }
}
