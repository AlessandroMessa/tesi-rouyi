package com.ruoyi.system.shared.vo;

public interface  SecurityPort {

    Long getCurrentUserId();
    boolean isCurrentUserAdmin();
    boolean isAdmin(Long userId);
    String getCurrentUsername();
    String encryptPassword(String password);
}
