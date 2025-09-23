package com.ruoyi.system.application.adapter;

import com.ruoyi.common.core.constant.UserConstants;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.system.api.domain.SysDept;
import com.ruoyi.system.domain.vo.TreeSelect;

import java.util.stream.Collectors;

public class TreeSelectFactory {
    public static TreeSelect fromSysDept(SysDept dept) {
        TreeSelect node = new TreeSelect();
        node.setId(dept.getDeptId());
        node.setLabel(dept.getDeptName());
        node.setDisabled(StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus()));
        node.setChildren(
                dept.getChildren().stream()
                        .map(TreeSelectFactory::fromSysDept)
                        .collect(Collectors.toList())
        );
        return node;
    }
}