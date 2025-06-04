package com.VDT_2025_Phase_1.DuongHaiAnh.controller.client;

import com.VDT_2025_Phase_1.DuongHaiAnh.dto.ConfigProfileDTO;
import com.VDT_2025_Phase_1.DuongHaiAnh.dto.ConfigServiceDTO;
import com.VDT_2025_Phase_1.DuongHaiAnh.payload.request.ConfigProfileRequest;
import com.VDT_2025_Phase_1.DuongHaiAnh.payload.response.ResponseData;
import com.VDT_2025_Phase_1.DuongHaiAnh.service.ConfigProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/config")
public class ProfileController {

    @Autowired
    private ConfigProfileService configProfileService;

    @GetMapping("/services/{serviceName}/profiles")
    public ResponseEntity<?> getAllServicesProfile(@PathVariable String serviceName){
        List<ConfigProfileDTO> configProfiles = configProfileService.getAllServicesProfile(serviceName);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseData.success("Get all profiles for service " + serviceName + " successfully", configProfiles));
    }

    @PostMapping("/services/{serviceName}/profiles")
    public ResponseEntity<?> createNewServiceProfile(@PathVariable String serviceName, @RequestBody ConfigProfileRequest request){
        ConfigProfileDTO newProfile = configProfileService.createNewProfile(serviceName, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseData.success("Create new profile for service " + serviceName + " successfully", newProfile));
    }

    @GetMapping("/services/{serviceName}/profiles/{profileName}")
    public ResponseEntity<?> getDetailServiceProfile(@PathVariable String serviceName, @PathVariable String profileName){
        ConfigProfileDTO configProfile = configProfileService.getDetailServiceProfile(serviceName, profileName);
        if (configProfile != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseData.success("Get profile " + profileName + " for service " + serviceName + " successfully", configProfile));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseData.error("Profile " + profileName + " not found for service " + serviceName));
        }
    }

    @PatchMapping("/services/{serviceName}/profiles/{profileName}")
    public ResponseEntity<?> updateServiceProfile(@PathVariable String serviceName, @PathVariable String profileName, @RequestBody ConfigProfileRequest request){
        ConfigProfileDTO configProfile = configProfileService.updateDetailServiceProfile(serviceName, profileName, request);
        if (configProfile != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseData.success("Update profile " + profileName + " for service " + serviceName + " successfully", configProfile));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseData.error("Profile " + profileName + " not found for service " + serviceName));
        }
    }

    @DeleteMapping("/services/{serviceName}/profiles/{profileName}")
    public ResponseEntity<?> deleteServiceProfile(@PathVariable String serviceName, @PathVariable String profileName) {
        ConfigProfileDTO configProfile = configProfileService.deleteConfigProfile(serviceName, profileName);
        if (configProfile != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseData.success("Delete profile " + profileName + " for service " + serviceName + " successfully", configProfile));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseData.error("Profile " + profileName + " not found for service " + serviceName));
        }
    }

}
