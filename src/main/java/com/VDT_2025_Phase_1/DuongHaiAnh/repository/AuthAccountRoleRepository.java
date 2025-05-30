package com.VDT_2025_Phase_1.DuongHaiAnh.repository;

import com.VDT_2025_Phase_1.DuongHaiAnh.entity.AuthAccountRole;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.SerializableKey.AuthAccountRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthAccountRoleRepository extends JpaRepository<AuthAccountRole, AuthAccountRoleId> {

}
