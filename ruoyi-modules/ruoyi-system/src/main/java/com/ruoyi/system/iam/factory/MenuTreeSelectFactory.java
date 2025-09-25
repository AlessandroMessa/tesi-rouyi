package com.ruoyi.system.iam.factory;
import com.ruoyi.system.iam.domain.SysMenu; // la tua classe attuale
import com.ruoyi.system.iam.domain.TreeSelect;

import java.util.stream.Collectors;

public final class MenuTreeSelectFactory {
    private MenuTreeSelectFactory() {}

    public static TreeSelect fromSysMenu(SysMenu m) {
        if (m == null) return null;
        TreeSelect node = new TreeSelect();
        node.setId(m.getMenuId());
        node.setLabel(m.getMenuName());
        node.setDisabled("1".equals(m.getStatus()));
        node.setChildren(
                m.getChildren() == null ? null :
                        m.getChildren().stream()
                                .map(MenuTreeSelectFactory::fromSysMenu)
                                .collect(Collectors.toList()) // Java 8
        );
        return node;
    }
}

