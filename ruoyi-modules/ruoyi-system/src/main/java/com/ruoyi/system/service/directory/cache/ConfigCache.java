package com.ruoyi.system.service.directory.cache;

public interface ConfigCache {
    String get(String key);
    void set(String key, String value);
    void delete(String key);
    void deleteByPattern(String pattern);
}
