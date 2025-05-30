package com.VDT_2025_Phase_1.DuongHaiAnh.repository;

import com.VDT_2025_Phase_1.DuongHaiAnh.entity.AuthRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthRoleRepository extends JpaRepository<AuthRole, UUID> {
    Optional<AuthRole> findByName(String name);
}
