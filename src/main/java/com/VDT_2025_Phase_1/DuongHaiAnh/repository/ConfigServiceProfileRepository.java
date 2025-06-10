package com.VDT_2025_Phase_1.DuongHaiAnh.repository;

import com.VDT_2025_Phase_1.DuongHaiAnh.entity.ConfigServiceProfile;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.SerializableKey.ConfigServiceProfileId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigServiceProfileRepository extends JpaRepository<ConfigServiceProfile, ConfigServiceProfileId> {
    List<ConfigServiceProfile> findByService_NameAndService_Owner_Account(String serviceName, String account);
    boolean existsByProfile_NameAndService_NameAndService_Owner_Account(String profileName, String serviceName,String account);
    ConfigServiceProfile findByProfile_NameAndService_NameAndService_Owner_Account(String profileName, String serviceName, String account);
}
