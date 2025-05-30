package com.VDT_2025_Phase_1.DuongHaiAnh.service.imp;

import com.VDT_2025_Phase_1.DuongHaiAnh.service.CachingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisCachingService implements CachingService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void saveKey(String key, String value, long duration, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, duration, timeUnit);
    }

    @Override
    public String getKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }
}
