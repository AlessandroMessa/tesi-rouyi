package com.ruoyi.system.directory.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import com.ruoyi.system.directory.domain.model.DictData;
import com.ruoyi.system.directory.domain.model.DictType;
import com.ruoyi.system.directory.domain.port.DictDataRepository;
import com.ruoyi.system.directory.domain.port.DictPort;
import com.ruoyi.system.directory.domain.port.DictTypeRepository;
import com.ruoyi.system.directory.domain.port.DirectoryServiceException;
import com.ruoyi.system.directory.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysDictTypeServiceImpl implements ISysDictTypeService {

    @Autowired private DictPort dictPort;
    @Autowired private DictDataRepository dictDataRepository;
    @Autowired private DictTypeRepository dictTypeRepository;

    @PostConstruct
    public void init() {
        loadingDictCache();
    }
    @Override
    public List<DictType> selectDictTypeList(DictType probe) {
        return dictTypeRepository.findByExample(probe);
    }

    @Override
    public List<DictType> selectDictTypeAll() {
        return dictTypeRepository.findAll();
    }

    @Override
    public DictType selectDictTypeById(Long dictId) {
        return dictTypeRepository.findById(dictId);
    }

    @Override
    public DictType selectDictTypeByType(String dictType) {
        return dictTypeRepository.findByType(dictType);
    }

    // --- Letture sui DictData ---

    @Override
    public List<DictData> selectDictDataByType(String dictType) {
        List<DictData> cached = dictPort.get(dictType);
        if (cached != null && !cached.isEmpty()) return cached;

        List<DictData> data = dictDataRepository.findByType(dictType);
        if (!data.isEmpty()) dictPort.set(dictType, data);
        return data; // mai null
    }

    // --- Cache ---

    @Override
    public void loadingDictCache() {
        List<DictData> allEnabled = dictDataRepository.findAllEnabled();
        Map<String, List<DictData>> byType = allEnabled.stream()
                .collect(Collectors.groupingBy(DictData::getDictType));

        byType.forEach((type, list) -> {
            List<DictData> sorted = list.stream()
                    .sorted(Comparator.comparing(DictData::getDictSort))
                    .collect(Collectors.toList());
            dictPort.set(type, sorted);
        });
    }

    @Override
    public void clearDictCache() {
        dictPort.clearAll();
    }

    @Override
    public void resetDictCache() {
        clearDictCache();
        loadingDictCache();
    }

    @Override
    public int insertDictType(DictType dict) {
        int row = dictTypeRepository.insert(dict);
        if (row > 0) {
            // invalidiamo la cache di quel type
            dictPort.set(dict.getDictType(), null);
        }
        return row;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDictType(DictType dict) {
        DictType oldDict = dictTypeRepository.findById(dict.getDictId());
        dictDataRepository.updateType(oldDict.getDictType(), dict.getDictType());
        int row = dictTypeRepository.update(dict);
        if (row > 0) {
            List<DictData> dictDatas = dictDataRepository.findByType(dict.getDictType());
            dictPort.set(dict.getDictType(), dictDatas);
        }
        return row;
    }

    @Override
    public void deleteDictTypeByIds(Long[] dictIds) {
        for (Long dictId : dictIds) {
            DictType dictType = dictTypeRepository.findById(dictId);
            if (dictDataRepository.countByType(dictType.getDictType()) > 0) {
                throw new DirectoryServiceException(
                        String.format("%s已分配,不能删除", dictType.getDictName()));
            }
            dictTypeRepository.deleteById(dictId);
            dictPort.remove(dictType.getDictType());
        }
    }


    @Override
    public boolean checkDictTypeUnique(DictType dict) {
        Long excludingId = dict.getDictId() == null ? -1L : dict.getDictId();
        // true se NON esiste un altro con lo stesso type
        return !dictTypeRepository.existsAnotherWithType(dict.getDictType(), excludingId);
    }
}
