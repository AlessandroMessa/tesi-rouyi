package com.ruoyi.system.directory.adapter;

import com.ruoyi.system.api.domain.SysDictType;
import com.ruoyi.system.directory.domain.model.DictType;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public final class DictTypeTranslator {
    private DictTypeTranslator() {}

    private static LocalDateTime toLocalDateTime(Date date) {
        return date == null ? null :
                date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private static Date toDate(LocalDateTime ldt) {
        return ldt == null ? null :
                Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }
    public static DictType toDomain(SysDictType s) {
        if (s == null) return null;
        DictType d = new DictType();
        d.setDictId(s.getDictId());
        d.setDictName(s.getDictName());
        d.setDictType(s.getDictType());
        d.setStatus("0".equals(s.getStatus()) ? DictType.Status.ENABLED : DictType.Status.DISABLED);
        d.setCreateBy(s.getCreateBy());
        d.setCreateTime(toLocalDateTime(s.getCreateTime()));
        d.setUpdateBy(s.getUpdateBy());
        d.setUpdateTime(toLocalDateTime(s.getUpdateTime()));
        d.setRemark(s.getRemark());
        return d;
    }

    public static SysDictType toApi(DictType d) {
        if (d == null) return null;
        SysDictType s = new SysDictType();
        s.setDictId(d.getDictId());
        s.setDictName(d.getDictName());
        s.setDictType(d.getDictType());
        s.setStatus(d.getStatus() == DictType.Status.ENABLED ? "0" : "1");
        s.setCreateBy(d.getCreateBy());
        s.setCreateTime(toDate(d.getCreateTime()));
        s.setUpdateBy(d.getUpdateBy());
        s.setUpdateTime(toDate(d.getUpdateTime()));
        s.setRemark(d.getRemark());
        return s;
    }
}
