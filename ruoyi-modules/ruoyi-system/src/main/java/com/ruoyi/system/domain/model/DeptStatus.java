package com.ruoyi.system.domain.model;

public enum DeptStatus {
    NORMAL,     // 0
    DISABLED;   // 1

    public static DeptStatus fromCode(String code) {
        return "0".equals(code) ? NORMAL : DISABLED;
    }

    public String toCode() {
        return this == NORMAL ? "0" : "1";
    }
}
