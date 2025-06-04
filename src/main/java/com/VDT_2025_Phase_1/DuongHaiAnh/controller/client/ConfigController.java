package com.VDT_2025_Phase_1.DuongHaiAnh.controller.client;

import com.VDT_2025_Phase_1.DuongHaiAnh.dto.ConfigServiceDTO;
import com.VDT_2025_Phase_1.DuongHaiAnh.payload.request.ConfigServiceRequest;
import com.VDT_2025_Phase_1.DuongHaiAnh.payload.response.ResponseData;
import com.VDT_2025_Phase_1.DuongHaiAnh.service.ConfigSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private ConfigSystemService configSystemService;

    @GetMapping("/services")
    public ResponseEntity<?> getAllServices(){
        List<ConfigServiceDTO> configServicesList= configSystemService.getAllServices();
        return ResponseEntity.status(HttpStatus.OK).body(ResponseData.success("Get Services success fully", configServicesList));
    }

    @PostMapping("/services")
    public ResponseEntity<?> createNewService(@RequestBody ConfigServiceRequest configServiceRequest){
        ConfigServiceDTO configServiceDTO = configSystemService.createNewServices(configServiceRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseData.success("Create Service success fully", configServiceDTO));
    }

    @GetMapping("/services/{serviceName}")
    public ResponseEntity<?> getDetailService(@PathVariable String serviceName){
        ConfigServiceDTO configServiceDTO = configSystemService.getDetailService(serviceName);
        if (configServiceDTO != null) {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseData.success("Get Service detail success fully", configServiceDTO));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseData.error("Service not found"));
        }
    }

    @PatchMapping("/services/{serviceName}")
    public ResponseEntity<?> updateService(@PathVariable String serviceName, @RequestBody ConfigServiceRequest configServiceRequest){
        ConfigServiceDTO updatedService = configSystemService.updateService(serviceName, configServiceRequest);
        return null;
    }

    @DeleteMapping("/services/{serviceId}")
    public ResponseEntity<?> deleteService(@PathVariable String serviceId){
        return null;
    }
}
