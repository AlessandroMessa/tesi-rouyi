package com.ruoyi.system.dept.factory;

import com.ruoyi.common.core.constant.UserConstants;
import com.ruoyi.system.dept.domain.model.Dept;
import com.ruoyi.system.dept.domain.model.DeptStatus;
import com.ruoyi.system.dept.domain.model.TreeSelect;

import java.util.stream.Collectors;

public class TreeSelectFactory {

    public static TreeSelect fromDomain(Dept dept) {
        TreeSelect node = new TreeSelect();
        node.setId(dept.getId());
        node.setLabel(dept.getName());
        node.setDisabled(dept.getStatus() == DeptStatus.DISABLED
                || UserConstants.DEPT_DISABLE.equals(dept.getStatus().toCode()));
        node.setChildren(
                dept.getChildren().stream()
                        .map(TreeSelectFactory::fromDomain)
                        .collect(Collectors.toList())
        );
        return node;
    }
}
