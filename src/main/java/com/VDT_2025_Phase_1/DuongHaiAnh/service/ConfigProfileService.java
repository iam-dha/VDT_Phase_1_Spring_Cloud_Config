package com.VDT_2025_Phase_1.DuongHaiAnh.service;


import com.VDT_2025_Phase_1.DuongHaiAnh.dto.ConfigProfileDTO;
import com.VDT_2025_Phase_1.DuongHaiAnh.payload.request.ConfigProfileRequest;

import java.util.List;

public interface ConfigProfileService {
    List<ConfigProfileDTO> getAllServicesProfile(String serviceName);
    ConfigProfileDTO createNewProfile(String serviceName, ConfigProfileRequest request);
}
