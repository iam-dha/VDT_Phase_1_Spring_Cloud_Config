package com.VDT_2025_Phase_1.DuongHaiAnh.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
public class ConfigProfileDTO {
    private String name;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private ZonedDateTime addedAt;
}
