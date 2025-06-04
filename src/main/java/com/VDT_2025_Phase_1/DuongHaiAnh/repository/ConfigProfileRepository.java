package com.VDT_2025_Phase_1.DuongHaiAnh.repository;

import com.VDT_2025_Phase_1.DuongHaiAnh.entity.ConfigProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConfigProfileRepository extends JpaRepository<ConfigProfile, UUID> {

}
