package com.ruoyi.system.adapter;

import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.security.port.SecurityPort;
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
        return SysUser.isAdmin(userId);
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
