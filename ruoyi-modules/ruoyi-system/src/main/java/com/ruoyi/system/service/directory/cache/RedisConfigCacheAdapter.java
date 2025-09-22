package com.ruoyi.system.service.directory.cache;

import java.util.Collection;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.ruoyi.common.redis.service.RedisService;

@Component
public class RedisConfigCacheAdapter implements ConfigCache {

    private final RedisService redis;

    @Autowired
    public RedisConfigCacheAdapter(RedisService redis) {
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
