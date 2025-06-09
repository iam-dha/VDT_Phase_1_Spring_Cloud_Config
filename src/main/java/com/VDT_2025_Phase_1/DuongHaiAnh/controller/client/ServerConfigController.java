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

import java.util.*;

@RestController
@RequestMapping("")
public class ServerConfigController {

    @Autowired
    private SystemService systemService;

    @GetMapping("{service}/{profiles}")
    public ResponseEntity<?> getConfigFileWithoutLabel(
            @PathVariable String service,
            @PathVariable String profiles) {
        return getConfigFile(service, profiles, "main"); // default label
    }

    @GetMapping("{service}/{profiles}/{label}")
    public ResponseEntity<?> getConfigFile(
            @PathVariable String service,
            @PathVariable String profiles,
            @PathVariable String label) {
        String[] profileList = profiles.split(",");
        List<Map<String, Object>> propertySources = new ArrayList<>();
        for(String profile : profileList){
            Map<String, Object> source = systemService.getConfigContent(service, profile.trim());
            if (!source.isEmpty()) {
                propertySources.add(Map.of(
                        "name", service + "-" + profile,
                        "source", source
                ));
            }
        }
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("name", service);
        response.put("profiles", Arrays.asList(profileList));
        response.put("label", label);
        response.put("propertySources", propertySources);
        return ResponseEntity.ok(response);
    }
    //Test function
    //@GetMapping("{service}/{profile}/{label}")
    //public ResponseEntity<?> getConfigFile(
    //        @PathVariable String service,
    //        @PathVariable String profile,
    //        @PathVariable String label) {
    //
    //    Map<String, Object> source = new LinkedHashMap<>();
    //    source.put("server.port", 9090);
    //    source.put("spring.application.name", "e-Banking");
    //    source.put("spring.datasource.url", "jdbc:postgresql://localhost:5433/bankingDB");
    //    source.put("spring.datasource.username", "postgres");
    //    source.put("spring.datasource.password", "DHA07062k4");
    //    source.put("spring.datasource.driver-class-name", "org.postgresql.Driver");
    //    source.put("spring.jpa.hibernate.ddl-auto", "update");
    //    source.put("logging.level.org.springframework.http.converter.json", "DEBUG");
    //    source.put("logging.level.com.fasterxml.jackson.databind", "DEBUG");
    //    source.put("logging.level.org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod", "TRACE");
    //
    //    Map<String, Object> propertySource = new LinkedHashMap<>();
    //    propertySource.put("name", "database-config");
    //    propertySource.put("source", source);
    //
    //    Map<String, Object> response = new LinkedHashMap<>();
    //    response.put("name", service);
    //    response.put("profiles", List.of(profile));
    //    response.put("label", label);
    //    response.put("propertySources", List.of(propertySource));
    //
    //    return ResponseEntity.ok(response);
    //}


}
