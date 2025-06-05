package com.VDT_2025_Phase_1.DuongHaiAnh.controller.client;

import com.VDT_2025_Phase_1.DuongHaiAnh.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/config")
public class ServerConfigController {

    @Autowired
    private SystemService systemService;

    @GetMapping("/{account}/{service}/{profile}")
    public ResponseEntity<?> getConfigFile(@PathVariable String account,
                                           @PathVariable String service,
                                           @PathVariable String profile) {
        String content = systemService.getConfigContent(account, service, profile);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }
}
