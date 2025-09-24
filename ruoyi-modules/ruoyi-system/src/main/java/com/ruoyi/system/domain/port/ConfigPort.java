package com.ruoyi.system.domain.port;

public interface ConfigPort {
    String get(String key);
    void set(String key, String value);
    void delete(String key);
    void deleteByPattern(String pattern);
}
