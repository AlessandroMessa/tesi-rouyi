package com.ruoyi.system.directory.adapter;

import com.ruoyi.system.api.domain.SysDictData;
import com.ruoyi.system.directory.domain.model.DictData;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public final class DictDataTranslator {
    private DictDataTranslator(){}
    private static LocalDateTime toLocalDateTime(Date date) {
        return date == null ? null :
                date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private static Date toDate(LocalDateTime ldt) {
        return ldt == null ? null :
                Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static DictData toDomain(SysDictData s) {
        if (s == null) return null;
        DictData d = new DictData();
        d.setDictCode(s.getDictCode());
        d.setDictSort(s.getDictSort());
        d.setDictLabel(s.getDictLabel());
        d.setDictValue(s.getDictValue());
        d.setDictType(s.getDictType());
        d.setCssClass(s.getCssClass());
        d.setListClass(s.getListClass());
        d.setDefault("Y".equalsIgnoreCase(s.getIsDefault()));
        d.setStatus("0".equals(s.getStatus()) ? DictData.Status.ENABLED : DictData.Status.DISABLED);
        d.setCreateBy(s.getCreateBy());
        d.setCreateTime(toLocalDateTime(s.getCreateTime()));   // Date -> LDT
        d.setUpdateBy(s.getUpdateBy());
        d.setUpdateTime(toLocalDateTime(s.getUpdateTime()));   // Date -> LDT
        d.setRemark(s.getRemark());
        return d;
    }

    public static SysDictData toApi(DictData d) {
        if (d == null) return null;
        SysDictData s = new SysDictData();
        s.setDictCode(d.getDictCode());
        s.setDictSort(d.getDictSort());
        s.setDictLabel(d.getDictLabel());
        s.setDictValue(d.getDictValue());
        s.setDictType(d.getDictType());
        s.setCssClass(d.getCssClass());
        s.setListClass(d.getListClass());
        s.setIsDefault(d.isDefault() ? "Y" : "N");
        s.setStatus(d.getStatus() == DictData.Status.ENABLED ? "0" : "1");
        s.setCreateBy(d.getCreateBy());
        s.setCreateTime(toDate(d.getCreateTime()));            // LDT -> Date
        s.setUpdateBy(d.getUpdateBy());
        s.setUpdateTime(toDate(d.getUpdateTime()));            // LDT -> Date
        s.setRemark(d.getRemark());
        return s;
    }
}
