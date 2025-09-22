package com.ruoyi.system.directory.adapter;

import java.util.List;

import com.ruoyi.system.directory.port.DictPort;
import org.springframework.stereotype.Component;
import com.ruoyi.common.security.utils.DictUtils;
import com.ruoyi.system.api.domain.SysDictData;

@Component
public class DictUtilsPortAdapter implements DictPort {
    @Override
    public List<SysDictData> get(String dictType) {
        return DictUtils.getDictCache(dictType);
    }

    @Override
    public void set(String dictType, List<SysDictData> values) {
        DictUtils.setDictCache(dictType, values);
    }

    @Override
    public void remove(String dictType) {
        DictUtils.removeDictCache(dictType);
    }

    @Override
    public void clearAll() {
        DictUtils.clearDictCache();
    }
}
