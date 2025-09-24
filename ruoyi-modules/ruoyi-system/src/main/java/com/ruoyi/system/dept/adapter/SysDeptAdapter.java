package com.ruoyi.system.dept.adapter;

import com.ruoyi.system.api.domain.SysDept;
import com.ruoyi.system.dept.domain.model.Dept;
import com.ruoyi.system.dept.domain.model.DeptStatus;

import java.util.List;
import java.util.stream.Collectors;

public final class SysDeptAdapter {

    private SysDeptAdapter() {}

    public static Dept toDomain(SysDept dto) {
        if (dto == null) return null;
        return Dept.fromAncestorsString(
                dto.getDeptId(),
                dto.getParentId(),
                dto.getAncestors(),
                dto.getDeptName(),
                dto.getOrderNum(),
                dto.getLeader(),
                dto.getPhone(),
                dto.getEmail(),
                DeptStatus.fromCode(dto.getStatus()),
                "2".equals(dto.getDelFlag()),   // 0=exists, 2=deleted
                dto.getParentName()
        );
    }

    public static SysDept toSysDept(Dept domain) {
        if (domain == null) return null;
        SysDept dto = new SysDept();
        dto.setDeptId(domain.getId());
        dto.setParentId(domain.getParentId());
        dto.setAncestors(domain.ancestorsCsv());
        dto.setDeptName(domain.getName());
        dto.setOrderNum(domain.getOrderNum());
        dto.setLeader(domain.getLeader());
        dto.setPhone(domain.getPhone());
        dto.setEmail(domain.getEmail());
        dto.setStatus(domain.getStatus().toCode());
        dto.setDelFlag(domain.isDeleted() ? "2" : "0");
        dto.setParentName(domain.getParentName());

        // mappa anche i figli
        List<SysDept> childDtos = domain.getChildren()
                .stream()
                .map(SysDeptAdapter::toSysDept)
                .collect(Collectors.toList());
        dto.setChildren(childDtos);

        return dto;
    }
}
