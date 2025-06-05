package com.VDT_2025_Phase_1.DuongHaiAnh.dto;

import com.VDT_2025_Phase_1.DuongHaiAnh.enums.ConfigEntryType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ConfigEntryDTO {
    private UUID id;
    private String key;
    private String value;
    private ConfigEntryType type;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
