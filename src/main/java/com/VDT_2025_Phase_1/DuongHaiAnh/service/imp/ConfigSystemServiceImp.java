package com.VDT_2025_Phase_1.DuongHaiAnh.service.imp;

import com.VDT_2025_Phase_1.DuongHaiAnh.dto.ConfigServiceDTO;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.AuthAccount;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.ConfigService;
import com.VDT_2025_Phase_1.DuongHaiAnh.payload.request.ConfigServiceRequest;
import com.VDT_2025_Phase_1.DuongHaiAnh.repository.AuthAccountRepository;
import com.VDT_2025_Phase_1.DuongHaiAnh.repository.ConfigServiceRepository;
import com.VDT_2025_Phase_1.DuongHaiAnh.service.ConfigSystemService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConfigSystemServiceImp implements ConfigSystemService {

    @Autowired
    private AuthAccountRepository authAccountRepository;

    @Autowired
    private ConfigServiceRepository configServiceRepository;

    private ConfigServiceDTO parseToDTO(ConfigService service) {
        return ConfigServiceDTO.builder()
                .id(service.getId())
                .name(service.getName())
                .description(service.getDescription())
                .publicVisible(service.isPublicVisible())
                .updatedAt(service.getUpdatedAt())
                .createdAt(service.getCreatedAt())
                .build();
    }

    @Override
    public List<ConfigServiceDTO> getAllServices() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Invalid authentication");
        }
        String account = (String) authentication.getPrincipal();
        List<ConfigService> configServices = configServiceRepository.findByOwner_Account(account);
        return configServices.stream().map(
                service -> ConfigServiceDTO.builder()
                        .id(service.getId())
                        .name(service.getName())
                        .description(service.getDescription())
                        .publicVisible(service.isPublicVisible())
                        .updatedAt(service.getUpdatedAt())
                        .createdAt(service.getCreatedAt())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ConfigServiceDTO createNewServices( ConfigServiceRequest configServiceRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Invalid authentication");
        }
        String account = (String) authentication.getPrincipal();
        AuthAccount authAccount = authAccountRepository.findByAccount(account);
        if (authAccount == null) {
            throw new IllegalStateException("Account not found");
        }
        String configName = configServiceRequest.getName();
        if (configName == null || configName.isBlank()) {
            throw new IllegalArgumentException("Service name cannot be empty");
        }

        if (configServiceRepository.existsByNameAndOwner_Account(configName, account)) {
            throw new IllegalStateException("Service name already exists");
        }
        ConfigService newConfigService = ConfigService.builder()
                .name(configName)
                .description(configServiceRequest.getDescription())
                .publicVisible(configServiceRequest.isPublicVisible())
                .owner(authAccount)
                .createdAt(ZonedDateTime.now())
                .updatedAt(ZonedDateTime.now())
                .build();
        ConfigService savedService = configServiceRepository.save(newConfigService);
        return parseToDTO(savedService);
    }

    @Override
    public ConfigServiceDTO getDetailService(String serviceName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Invalid authentication");
        }
        String account = (String) authentication.getPrincipal();
        AuthAccount authAccount = authAccountRepository.findByAccount(account);
        if (authAccount == null) {
            throw new IllegalStateException("Account not found");
        }
        ConfigService configService = configServiceRepository.findByNameAndOwner_Account(serviceName, account);
        if (configService == null) {
            throw new IllegalArgumentException("Service not found");
        }
        return parseToDTO(configService);
    }

    @Override
    @Transactional
    public ConfigServiceDTO updateService(String serviceName, ConfigServiceRequest configServiceRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Invalid authentication");
        }
        String account = (String) authentication.getPrincipal();
        AuthAccount authAccount = authAccountRepository.findByAccount(account);
        if (authAccount == null) {
            throw new IllegalStateException("Account not found");
        }
        ConfigService configService = configServiceRepository.findByNameAndOwner_Account(serviceName, account);
        if (configService == null) {
            throw new IllegalArgumentException("Service not found");
        }
        if (configServiceRequest.getName() != null && !configServiceRequest.getName().isBlank()) {
            if (!configService.getName().equals(configServiceRequest.getName()) && configServiceRepository.existsByNameAndOwner_Account(configServiceRequest.getName(), account)) {
                throw new IllegalStateException("New service name already exists");
            }
            configService.setName(configServiceRequest.getName());
        }
        if (configServiceRequest.getDescription() != null) {
            configService.setDescription(configServiceRequest.getDescription());
        }
        configService.setPublicVisible(configServiceRequest.isPublicVisible());
        configService.setUpdatedAt(ZonedDateTime.now());
        ConfigService updatedConfig = configServiceRepository.save(configService);
        return parseToDTO(updatedConfig);
    }
    @Override
    @Transactional
    public ConfigServiceDTO deleteService(String serviceName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Invalid authentication");
        }
        String account = (String) authentication.getPrincipal();
        AuthAccount authAccount = authAccountRepository.findByAccount(account);
        if (authAccount == null) {
            throw new IllegalStateException("Account not found");
        }
        ConfigService configService = configServiceRepository.findByNameAndOwner_Account(serviceName, account);
        if (configService == null) {
            throw new IllegalArgumentException("Service not found");
        }
        configServiceRepository.delete(configService);
        return parseToDTO(configService);
    }
}

