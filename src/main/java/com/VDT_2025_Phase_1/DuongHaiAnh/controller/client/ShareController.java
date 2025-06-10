package com.VDT_2025_Phase_1.DuongHaiAnh.controller.client;

import com.VDT_2025_Phase_1.DuongHaiAnh.dto.ConfigEntryDTO;
import com.VDT_2025_Phase_1.DuongHaiAnh.payload.response.ResponseData;
import com.VDT_2025_Phase_1.DuongHaiAnh.service.ConfigEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/share")
public class ShareController {

    @Autowired
    private ConfigEntryService configEntryService;

    @GetMapping("/{account}/{service}/{profile}")
    public ResponseEntity<?> shareConfig(
            @PathVariable String account,
            @PathVariable String service,
            @PathVariable String profile) {
        List<ConfigEntryDTO> entries = configEntryService.gettAllSharedConfigEntries(account, service, profile);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseData.success("Get shared config entries successfully", entries));
    }
}
