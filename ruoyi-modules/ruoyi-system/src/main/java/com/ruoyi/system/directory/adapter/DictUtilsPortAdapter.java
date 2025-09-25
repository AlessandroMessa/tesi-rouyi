package com.ruoyi.system.directory.adapter;

import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.system.directory.domain.model.DictData;
import com.ruoyi.system.directory.domain.port.DictPort;
import org.springframework.stereotype.Component;
import com.ruoyi.common.security.utils.DictUtils;
import com.ruoyi.system.api.domain.SysDictData;

@Component
public class DictUtilsPortAdapter implements DictPort {
    @Override
    public List<DictData> get(String dictType) {
        List<SysDictData> sysList = DictUtils.getDictCache(dictType);
        return sysList.stream()
                .map(DictDataTranslator::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void set(String dictType, List<DictData> values) {
        List<SysDictData> sysList = values.stream()
                .map(DictDataTranslator::toApi)
                .collect(Collectors.toList());
        DictUtils.setDictCache(dictType, sysList);
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
