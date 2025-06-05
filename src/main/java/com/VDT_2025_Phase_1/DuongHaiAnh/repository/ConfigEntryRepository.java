package com.VDT_2025_Phase_1.DuongHaiAnh.repository;

import com.VDT_2025_Phase_1.DuongHaiAnh.entity.ConfigEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ConfigEntryRepository extends JpaRepository<ConfigEntry, UUID> {
    List<ConfigEntry> findByProfile_Name(String profileName);
}
