package com.ruoyi.system.directory.service.impl;

import java.util.List;
import com.ruoyi.system.directory.domain.model.DictData;
import com.ruoyi.system.directory.domain.port.DictDataRepository;
import com.ruoyi.system.directory.domain.port.DictPort;
import com.ruoyi.system.directory.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysDictDataServiceImpl implements ISysDictDataService {

    @Autowired private DictDataRepository dictDataRepository;
    @Autowired private DictPort dictPort;

    @Override
    public List<DictData> selectDictDataList(DictData probe) {
        return dictDataRepository.findByExample(probe);
    }

    @Override
    public String selectDictLabel(String dictType, String dictValue) {
        return dictDataRepository.findLabel(dictType, dictValue);
    }

    @Override
    public DictData selectDictDataById(Long dictCode) {
        return dictDataRepository.findById(dictCode);
    }

    @Override
    public void deleteDictDataByIds(Long[] dictCodes) {
        for (Long dictCode : dictCodes) {
            DictData data = dictDataRepository.findById(dictCode);
            if (data == null) continue;

            dictDataRepository.deleteById(dictCode);

            // refresh cache del type coinvolto
            List<DictData> dictDatas = dictDataRepository.findByType(data.getDictType());
            dictPort.set(data.getDictType(), dictDatas);
        }
    }

    @Override
    public int insertDictData(DictData data) {
        int row = dictDataRepository.insert(data);
        if (row > 0) {
            List<DictData> dictDatas = dictDataRepository.findByType(data.getDictType());
            dictPort.set(data.getDictType(), dictDatas);
        }
        return row;
    }

    @Override
    public int updateDictData(DictData data) {
        int row = dictDataRepository.update(data);
        if (row > 0) {
            List<DictData> dictDatas = dictDataRepository.findByType(data.getDictType());
            dictPort.set(data.getDictType(), dictDatas);
        }
        return row;
    }
}
