package com.ruoyi.system.directory.domain.port;

import java.util.List;
import com.ruoyi.system.api.domain.SysDictData;

public interface DictPort {
    List<SysDictData> get(String dictType);
    void set(String dictType, List<SysDictData> values);
    void remove(String dictType);
    void clearAll();
}