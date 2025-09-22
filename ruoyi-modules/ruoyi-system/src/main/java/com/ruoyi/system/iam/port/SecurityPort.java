package com.ruoyi.system.iam.port;

public interface  SecurityPort {

    Long getCurrentUserId();
    boolean isCurrentUserAdmin();
    boolean isAdmin(Long userId);
    String getCurrentUsername();
    String encryptPassword(String password);
}
