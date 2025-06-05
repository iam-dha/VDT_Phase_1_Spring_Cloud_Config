package com.VDT_2025_Phase_1.DuongHaiAnh.service;

import com.VDT_2025_Phase_1.DuongHaiAnh.dto.ConfigEntryDTO;
import com.VDT_2025_Phase_1.DuongHaiAnh.entity.ConfigEntry;
import com.VDT_2025_Phase_1.DuongHaiAnh.payload.request.ConfigEntryRequest;

import java.util.List;

public interface ConfigEntryService {
    List<ConfigEntryDTO> getAllConfigEntries(String serviceName, String profileName);
    void updateConfigEntry(String serviceName, String profileName, List<ConfigEntryRequest> requests);
}
