package com.VDT_2025_Phase_1.DuongHaiAnh.service;

import com.VDT_2025_Phase_1.DuongHaiAnh.dto.ConfigServiceDTO;
import com.VDT_2025_Phase_1.DuongHaiAnh.payload.request.ConfigServiceRequest;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ConfigSystemService {
    List<ConfigServiceDTO> getAllServices();
    ConfigServiceDTO createNewServices( ConfigServiceRequest configServiceRequest);
    ConfigServiceDTO getDetailService(String serviceName);
    ConfigServiceDTO updateService(String serviceName, ConfigServiceRequest configServiceRequest);
    ConfigServiceDTO deleteService(String serviceName);
}
