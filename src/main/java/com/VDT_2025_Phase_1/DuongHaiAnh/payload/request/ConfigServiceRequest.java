package com.VDT_2025_Phase_1.DuongHaiAnh.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
public class ConfigServiceRequest {
    private String name;
    private String description;
    private boolean publicVisible = false;
}
