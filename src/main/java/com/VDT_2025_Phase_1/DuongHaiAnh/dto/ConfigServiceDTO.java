package com.VDT_2025_Phase_1.DuongHaiAnh.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ConfigServiceDTO {
    private UUID id;
    private String name;
    private String description;
    private boolean publicVisible = false;
    private ZonedDateTime updatedAt;
    private ZonedDateTime createdAt;
}
