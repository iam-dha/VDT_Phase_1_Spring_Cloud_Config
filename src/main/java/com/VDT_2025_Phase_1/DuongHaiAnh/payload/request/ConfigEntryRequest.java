package com.VDT_2025_Phase_1.DuongHaiAnh.payload.request;

import com.VDT_2025_Phase_1.DuongHaiAnh.enums.ConfigEntryType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ConfigEntryRequest {
    private UUID id;
    private String key;
    private String value;
    private ConfigEntryType type;
}
