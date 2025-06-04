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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
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
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }
        String account = (String) authentication.getPrincipal();
        if (account == null || account.isEmpty()) {
            throw new IllegalStateException("Account information is missing");
        }
        ConfigService configService = configServiceRepository.findByName(serviceName);
        if (configService == null) {
            throw new IllegalArgumentException("Service not found: " + serviceName);
        }
        if( !configService.getOwner().getAccount().equals(account)){
            throw new AccessDeniedException("You do not have permission to access this service: " + serviceName);
        }
        List<ConfigServiceProfile> configServiceProfiles = configServiceProfileRepository.findByService_Name(serviceName);
        return configServiceProfiles.stream()
                .map(this::parseToDTO)
                .toList();
    }

    @Override
    @Transactional
    public ConfigProfileDTO createNewProfile(String serviceName, ConfigProfileRequest request) {
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }
        String account = (String) authentication.getPrincipal();
        if (account == null || account.isEmpty()) {
            throw new IllegalStateException("Account information is missing");
        }
        ConfigService configService = configServiceRepository.findByName(serviceName);
        if (configService == null) {
            throw new IllegalArgumentException("Service not found: " + serviceName);
        }
        if( !configService.getOwner().getAccount().equals(account)){
            throw new AccessDeniedException("You do not have permission to access this service: " + serviceName);
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
                .serviceId(configService.getId())
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

    @Override
    public ConfigProfileDTO getDetailServiceProfile(String serviceName, String profileName) {
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }
        String account = (String) authentication.getPrincipal();
        if (account == null || account.isEmpty()) {
            throw new IllegalStateException("Account information is missing");
        }
        ConfigServiceProfile configProfileService = configServiceProfileRepository.findByProfile_NameAndService_Name(profileName, serviceName);
        if (configProfileService == null) {
            throw new IllegalArgumentException("Profile " + profileName + " not found for service " + serviceName);
        }
        if(!configProfileService.getService().getOwner().getAccount().equals(account)){
            throw new AccessDeniedException("You do not have permission to access this profile: " + profileName + " for service " + serviceName);
        }
        return parseToDTO(configProfileService);
    }

    @Override
    @Transactional
    public ConfigProfileDTO updateDetailServiceProfile(String serviceName, String profileName, ConfigProfileRequest request) {
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }
        String account = (String) authentication.getPrincipal();
        if (account == null || account.isEmpty()) {
            throw new IllegalStateException("Account information is missing");
        }
        ConfigServiceProfile configServiceProfile = configServiceProfileRepository.findByProfile_NameAndService_Name(profileName,  serviceName);
        if (configServiceProfile == null) {
            throw new IllegalArgumentException("Profile " + profileName + " not found for service " + serviceName);
        }
        if(!configServiceProfile.getService().getOwner().getAccount().equals(account)){
            throw new AccessDeniedException("You do not have permission to access this profile: " + profileName + " for service " + serviceName);
        }
        ConfigProfile profile = configServiceProfile.getProfile();
        if (profile == null) {
            throw new IllegalStateException("ConfigProfile is missing in ConfigServiceProfile");
        }
        String newProfileName = request.getName();
        if(!newProfileName.equals(profileName) && configServiceProfileRepository.existsByProfile_NameAndService_Name(newProfileName, serviceName)){
            throw new IllegalArgumentException("Profile with name " + newProfileName + " already exists for service " + serviceName);
        }
        profile.setUpdatedAt(ZonedDateTime.now());
        profile.setName(newProfileName);
        ConfigProfile savedProfile = configProfileRepository.save(profile);
        return ConfigProfileDTO.builder()
                .addedAt(configServiceProfile.getAddedAt())
                .updatedAt(savedProfile.getUpdatedAt())
                .name(newProfileName)
                .createdAt(savedProfile.getCreatedAt())
                .build();
    }

    @Override
    public ConfigProfileDTO deleteConfigProfile(String serviceName, String profileName) {
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }
        String account = (String) authentication.getPrincipal();
        if (account == null || account.isEmpty()) {
            throw new IllegalStateException("Account information is missing");
        }
        ConfigServiceProfile configServiceProfile = configServiceProfileRepository.findByProfile_NameAndService_Name(profileName, serviceName);
        if (configServiceProfile == null) {
            throw new IllegalArgumentException("Profile " + profileName + " not found for service " + serviceName);
        }
        if(!configServiceProfile.getService().getOwner().getAccount().equals(account)){
            throw new AccessDeniedException("You do not have permission to access this profile: " + profileName + " for service " + serviceName);
        }
        ConfigProfile profile = configServiceProfile.getProfile();
        if (profile == null) {
            throw new IllegalStateException("ConfigProfile is missing in ConfigServiceProfile");
        }
        configServiceProfileRepository.delete(configServiceProfile);
        return ConfigProfileDTO.builder()
                .name(profile.getName())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .addedAt(configServiceProfile.getAddedAt())
                .build();
    }

}
