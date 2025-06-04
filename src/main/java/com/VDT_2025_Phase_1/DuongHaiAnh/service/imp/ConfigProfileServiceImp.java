package com.VDT_2025_Phase_1.DuongHaiAnh.service.imp;

import com.VDT_2025_Phase_1.DuongHaiAnh.dto.ConfigProfileDTO;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.ConfigProfile;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.ConfigService;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.ConfigServiceProfile;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.SerializableKey.ConfigServiceProfileId;
import com.VDT_2025_Phase_1.DuongHaiAnh.payload.request.ConfigProfileRequest;
import com.VDT_2025_Phase_1.DuongHaiAnh.repository.ConfigProfileRepository;
import com.VDT_2025_Phase_1.DuongHaiAnh.repository.ConfigServiceProfileRepository;
import com.VDT_2025_Phase_1.DuongHaiAnh.repository.ConfigServiceRepository;
import com.VDT_2025_Phase_1.DuongHaiAnh.service.ConfigProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ConfigProfileServiceImp implements ConfigProfileService {

    public ConfigProfileDTO parseToDTO(ConfigServiceProfile configServiceProfile) {
        ConfigProfile profile = configServiceProfile.getProfile();
        if (profile == null) {
            throw new IllegalStateException("ConfigProfile is missing in ConfigServiceProfile");
        }
        return ConfigProfileDTO.builder()
                .name(profile.getName())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .addedAt(configServiceProfile.getAddedAt())
                .build();
    }

    @Autowired
    private ConfigServiceProfileRepository configServiceProfileRepository;

    @Autowired
    private ConfigProfileRepository configProfileRepository;

    @Autowired
    private ConfigServiceRepository configServiceRepository;

    @Override
    public List<ConfigProfileDTO> getAllServicesProfile(String serviceName) {
        ConfigService configService = configServiceRepository.findByName(serviceName);
        if (configService == null) {
            throw new IllegalArgumentException("Service not found: " + serviceName);
        }
        List<ConfigServiceProfile> configServiceProfiles = configServiceProfileRepository.findByService_Name(serviceName);
        return configServiceProfiles.stream()
                .map(this::parseToDTO)
                .toList();
    }

    @Override
    public ConfigProfileDTO createNewProfile(String serviceName, ConfigProfileRequest request) {
        ConfigService service = configServiceRepository.findByName(serviceName);
        if (service == null) {
            throw new IllegalArgumentException("Service not found: " + serviceName);
        }
        String profileName = request.getName();
        if (configServiceProfileRepository.existsByProfile_NameAndService_Name(profileName, serviceName)) {
            throw new IllegalArgumentException("Profile with name " + profileName + " already exists for service " + serviceName);
        }
        ConfigProfile newProfile = ConfigProfile.builder()
                .name(profileName)
                .createdAt(ZonedDateTime.now())
                .updatedAt(ZonedDateTime.now())
                .build();
        ConfigProfile savedProfile = configProfileRepository.save(newProfile);
        ConfigServiceProfileId id = ConfigServiceProfileId.builder()
                .serviceId(service.getId())
                .profileId(savedProfile.getId())
                .build();
        ConfigServiceProfile newConfigServiceProfile = ConfigServiceProfile.builder()
                .id(id)
                .addedAt(ZonedDateTime.now())
                .build();
        ConfigServiceProfile savedConfigServiceProfile = configServiceProfileRepository.save(newConfigServiceProfile);
        return ConfigProfileDTO.builder()
                .addedAt(savedConfigServiceProfile.getAddedAt())
                .createdAt(savedProfile.getCreatedAt())
                .updatedAt(savedProfile.getUpdatedAt())
                .name(profileName)
                .build();
    }


}
