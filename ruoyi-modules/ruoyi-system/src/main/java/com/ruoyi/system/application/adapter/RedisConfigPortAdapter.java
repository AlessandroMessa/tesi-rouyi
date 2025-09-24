package com.ruoyi.system.application.adapter;

import com.ruoyi.common.redis.service.RedisService;
import com.ruoyi.system.domain.port.ConfigPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class RedisConfigPortAdapter implements ConfigPort {

    private final RedisService redis;

    @Autowired
    public RedisConfigPortAdapter(RedisService redis) {
        this.redis = redis;
    }

    @Override
    public String get(String key) {
        Object v = redis.getCacheObject(key);
        return v == null ? null : String.valueOf(v);
    }

    @Override
    public void set(String key, String value) {
        redis.setCacheObject(key, value);
    }

    @Override
    public void delete(String key) {
        redis.deleteObject(key);
    }

    @Override
    public void deleteByPattern(String pattern) {
        Collection<String> keys = redis.keys(pattern);
        redis.deleteObject(keys);
    }
}
