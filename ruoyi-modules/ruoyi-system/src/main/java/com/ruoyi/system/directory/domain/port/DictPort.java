package com.ruoyi.system.directory.domain.port;

import java.util.List;
import com.ruoyi.system.directory.domain.model.DictData;

public interface DictPort {
    List<DictData> get(String dictType);
    void set(String dictType, List<DictData> values);
    void remove(String dictType);
    void clearAll();
}