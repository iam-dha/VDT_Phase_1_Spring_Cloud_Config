package com.VDT_2025_Phase_1.DuongHaiAnh.dto;

import com.VDT_2025_Phase_1.DuongHaiAnh.entity.AuthAccount;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class RoleDTO {
    private String name;
    private String description;
    private ZonedDateTime createAt;
    List<String> permissions;
}
