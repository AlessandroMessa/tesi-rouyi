package com.ruoyi.system.directory.adapter;

import com.ruoyi.system.api.domain.SysDictData;
import com.ruoyi.system.directory.domain.model.DictData;
import com.ruoyi.system.directory.domain.port.DictDataRepository;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DictDataRepositoryImpl implements DictDataRepository {

    private final SysDictDataMapper mapper;

    public DictDataRepositoryImpl(SysDictDataMapper mapper) {
        this.mapper = mapper;
    }

    // ---- Letture di supporto cache / query semplici ----
    @Override
    public List<DictData> findAllEnabled() {
        SysDictData probe = new SysDictData();
        probe.setStatus("0"); // "0" = ENABLED nello schema legacy
        List<SysDictData> rows = mapper.selectDictDataList(probe);
        if (rows == null || rows.isEmpty()) return Collections.emptyList();
        return rows.stream().map(DictDataTranslator::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<DictData> findByType(String type) {
        List<SysDictData> rows = mapper.selectDictDataByType(type);
        if (rows == null || rows.isEmpty()) return Collections.emptyList();
        return rows.stream().map(DictDataTranslator::toDomain).collect(Collectors.toList());
    }

    @Override
    public int countByType(String type) {
        return mapper.countDictDataByType(type);
    }

    @Override
    public void updateType(String oldType, String newType) {
        mapper.updateDictDataType(oldType, newType);
    }

    // ---- Metodi aggiuntivi richiesti dal service ----
    @Override
    public List<DictData> findByExample(DictData probe) {
        SysDictData apiProbe = DictDataTranslator.toApi(probe);
        List<SysDictData> rows = mapper.selectDictDataList(apiProbe);
        if (rows == null || rows.isEmpty()) return Collections.emptyList();
        return rows.stream().map(DictDataTranslator::toDomain).collect(Collectors.toList());
    }

    @Override
    public String findLabel(String type, String value) {
        return mapper.selectDictLabel(type, value);
    }

    @Override
    public DictData findById(Long dictCode) {
        SysDictData s = mapper.selectDictDataById(dictCode);
        return DictDataTranslator.toDomain(s);
    }

    @Override
    public int deleteById(Long dictCode) {
        return mapper.deleteDictDataById(dictCode);
    }

    @Override
    public int insert(DictData data) {
        SysDictData s = DictDataTranslator.toApi(data);
        return mapper.insertDictData(s);
    }

    @Override
    public int update(DictData data) {
        SysDictData s = DictDataTranslator.toApi(data);
        return mapper.updateDictData(s);
    }
}
