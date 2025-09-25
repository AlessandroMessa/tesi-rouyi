package com.ruoyi.system.directory.domain.port;

import com.ruoyi.system.directory.domain.model.DictData;

import java.util.List;

public interface DictDataRepository {
    List<DictData> findByType(String type);
    List<DictData> findAllEnabled();
    int countByType(String type);
    void updateType(String oldType, String newType);
    List<DictData> findByExample(DictData probe);
    String findLabel(String type, String value);
    DictData findById(Long dictCode);
    int deleteById(Long dictCode);
    int insert(DictData data);
    int update(DictData data);

}
