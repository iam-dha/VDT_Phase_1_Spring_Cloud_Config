package com.VDT_2025_Phase_1.DuongHaiAnh.controller.client;

import com.VDT_2025_Phase_1.DuongHaiAnh.dto.UserDetailDTO;
import com.VDT_2025_Phase_1.DuongHaiAnh.payload.response.ResponseData;
import com.VDT_2025_Phase_1.DuongHaiAnh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("")
    public ResponseEntity<?> getUserInformation(){
        UserDetailDTO userDetailDTO = userService.getUserInfomation();
        return ResponseEntity.status(HttpStatus.OK).body(ResponseData.success("Get User Information successfully",userDetailDTO));
    }
}
