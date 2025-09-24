package com.ruoyi.system.directory.domain.port;

public interface ConfigPort {
    String get(String key);
    void set(String key, String value);
    void delete(String key);
    void deleteByPattern(String pattern);
}
