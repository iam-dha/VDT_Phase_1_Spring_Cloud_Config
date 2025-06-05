package com.VDT_2025_Phase_1.DuongHaiAnh.service.imp;

import com.VDT_2025_Phase_1.DuongHaiAnh.dto.ConfigEntryDTO;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.ConfigEntry;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.ConfigProfile;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.ConfigService;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.ConfigServiceProfile;
import com.VDT_2025_Phase_1.DuongHaiAnh.payload.request.ConfigEntryRequest;
import com.VDT_2025_Phase_1.DuongHaiAnh.repository.ConfigProfileRepository;
import com.VDT_2025_Phase_1.DuongHaiAnh.repository.ConfigServiceProfileRepository;
import com.VDT_2025_Phase_1.DuongHaiAnh.repository.ConfigServiceRepository;
import com.VDT_2025_Phase_1.DuongHaiAnh.service.ConfigEntryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConfigEntryServiceImp implements ConfigEntryService {

    @Autowired
    private ConfigProfileRepository configProfileRepository;

    @Autowired
    private ConfigServiceRepository configServiceRepository;

    @Autowired
    private ConfigServiceProfileRepository configServiceProfileRepository;

    private ConfigEntryDTO parseToDTO(ConfigEntry entry){
        return ConfigEntryDTO.builder()
                .id(entry.getId())
                .key(entry.getKey())
                .value(entry.getValue())
                .type(entry.getType())
                .createdAt(entry.getCreatedAt())
                .updatedAt(entry.getUpdatedAt())
                .build();
    }

    @Override
    public List<ConfigEntryDTO> getAllConfigEntries(String serviceName, String profileName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }
        String account = (String) authentication.getPrincipal();
        if (account == null || account.isEmpty()) {
            throw new IllegalStateException("Account information is missing");
        }
        ConfigServiceProfile configProfileService = configServiceProfileRepository.findByProfile_NameAndService_Name(profileName, serviceName);
        if(configProfileService == null){
            throw new IllegalStateException("Profile " + profileName + " for service " + serviceName + " does not exist");
        }
        if(!configProfileService.getService().getOwner().getAccount().equals(account)) {
            throw new IllegalStateException("You do not have permission to access this profile");
        }
        List<ConfigEntry> entries = configProfileService.getProfile().getEntries();
        return entries.stream().map(this::parseToDTO).toList();
    }

    @Override
    @Transactional
    public void updateConfigEntry(String serviceName, String profileName, List<ConfigEntryRequest> requests) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }

        String account = (String) authentication.getPrincipal();
        if (account == null || account.isEmpty()) {
            throw new IllegalStateException("Account information is missing");
        }

        ConfigServiceProfile configProfileService = configServiceProfileRepository.findByProfile_NameAndService_Name(profileName, serviceName);
        if (configProfileService == null) {
            throw new IllegalStateException("Profile " + profileName + " for service " + serviceName + " does not exist");
        }

        if (!configProfileService.getService().getOwner().getAccount().equals(account)) {
            throw new IllegalStateException("You do not have permission to access this profile");
        }

        ConfigProfile profile = configProfileService.getProfile();
        List<ConfigEntry> entryList = profile.getEntries();

        Set<UUID> requestIds = requests.stream()
                .map(ConfigEntryRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        entryList.removeIf(entry -> entry.getId() != null && !requestIds.contains(entry.getId()));

        for (ConfigEntryRequest req : requests) {
            if (req.getId() == null) {
                boolean keyExists = entryList.stream()
                        .anyMatch(e -> e.getKey().equals(req.getKey()));

                if (keyExists) {
                    throw new IllegalArgumentException("Key '" + req.getKey() + "' already exists. Use existing ID to update.");
                }
                ConfigEntry newEntry = ConfigEntry.builder()
                        .key(req.getKey())
                        .value(req.getValue())
                        .type(req.getType())
                        .profile(profile)
                        .createdAt(ZonedDateTime.now())
                        .updatedAt(ZonedDateTime.now())
                        .build();
                entryList.add(newEntry);
            } else {
                // Cập nhật
                ConfigEntry existing = entryList.stream()
                        .filter(e -> req.getId().equals(e.getId()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Entry not found: " + req.getId()));

                existing.setKey(req.getKey());
                existing.setValue(req.getValue());
                existing.setType(req.getType());
                existing.setUpdatedAt(ZonedDateTime.now());
            }
        }

        // Không cần set lại list entries → giữ nguyên reference, chỉ thao tác trên nó
        configProfileRepository.save(profile);
    }
}
