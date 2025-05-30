package com.VDT_2025_Phase_1.DuongHaiAnh.service;

import java.util.concurrent.TimeUnit;

public interface CachingService {
    void saveKey(String key, String value, long duration, TimeUnit timeUnit);
    String getKey(String key);
    void deleteKey(String key);
}
