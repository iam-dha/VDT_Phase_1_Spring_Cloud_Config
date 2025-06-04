package com.VDT_2025_Phase_1.DuongHaiAnh.repository;

import com.VDT_2025_Phase_1.DuongHaiAnh.entity.AuthAccount;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.ConfigService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConfigServiceRepository extends JpaRepository<ConfigService, UUID> {
    List<ConfigService> findByOwner_Account(String account);
    boolean existsByName(String name);
    ConfigService findByName(String name);
}
