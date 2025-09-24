package com.ruoyi.system.common;

public interface  SecurityPort {

    Long getCurrentUserId();
    boolean isCurrentUserAdmin();
    boolean isAdmin(Long userId);
    String getCurrentUsername();
    String encryptPassword(String password);
}
