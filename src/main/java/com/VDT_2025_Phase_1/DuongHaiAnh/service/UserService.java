package com.VDT_2025_Phase_1.DuongHaiAnh.service;

import com.VDT_2025_Phase_1.DuongHaiAnh.dto.UserDetailDTO;

import java.util.List;

public interface UserService {
    List<UserDetailDTO> getAllUser();
    UserDetailDTO getUserInfomation();
}
