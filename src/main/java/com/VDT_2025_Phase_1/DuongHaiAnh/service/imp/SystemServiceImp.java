package com.VDT_2025_Phase_1.DuongHaiAnh.service.imp;

import com.VDT_2025_Phase_1.DuongHaiAnh.entity.ConfigEntry;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.ConfigServiceProfile;
import com.VDT_2025_Phase_1.DuongHaiAnh.repository.ConfigServiceProfileRepository;
import com.VDT_2025_Phase_1.DuongHaiAnh.service.SystemService;
import com.VDT_2025_Phase_1.DuongHaiAnh.utils.component.YamlBuilderHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemServiceImp implements SystemService {

    @Autowired
    private YamlBuilderHelper yamlBuilderHelper;

    @Autowired
    private ConfigServiceProfileRepository configServiceProfileRepository;

    @Override
    public Map<String, Object> getConfigContent(String service, String profile) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("No authentication found.");
        }
        String account = (String) auth.getPrincipal();
        ConfigServiceProfile profileLink = configServiceProfileRepository
                .findByProfile_NameAndService_Name(profile, service);

        if (profileLink == null || !profileLink.getService().getOwner().getAccount().equals(account)) {
            throw new AccessDeniedException("Config not found or permission denied.");
        }

        List<ConfigEntry> entries = profileLink.getProfile().getEntries();
        Map<String, Object> configMap = new LinkedHashMap<>();
        for (ConfigEntry entry : entries) {
            configMap.put(entry.getKey(), entry.getValue());
        }
        return configMap;
    }
}
