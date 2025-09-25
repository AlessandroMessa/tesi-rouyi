package com.ruoyi.system.directory.domain.port;

import com.ruoyi.system.directory.domain.model.DictType;

import java.util.List;

public interface DictTypeRepository {
    DictType findById(Long id);
    DictType findByType(String type);
    List<DictType> findAll();
    List<DictType> findByExample(DictType probe);
    int insert(DictType dict);
    int update(DictType dict);
    int deleteById(Long id);
    boolean existsAnotherWithType(String type, Long excludingId);

}
