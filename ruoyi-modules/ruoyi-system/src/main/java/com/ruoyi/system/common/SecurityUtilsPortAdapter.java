package com.ruoyi.system.common;

import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtilsPortAdapter implements SecurityPort {
    @Override
    public Long getCurrentUserId() {
        return SecurityUtils.getUserId();
    }

    @Override
    public boolean isCurrentUserAdmin() {
        Long uid = SecurityUtils.getUserId();
        return isAdmin(uid);
    }

    @Override
    public boolean isAdmin(Long userId) {
        return SecurityUtils.isAdmin(userId);
    }

    @Override
    public String getCurrentUsername() {
        return SecurityUtils.getUsername();
    }
    @Override
    public String encryptPassword(String password){
        return SecurityUtils.encryptPassword(password);
    }
}
