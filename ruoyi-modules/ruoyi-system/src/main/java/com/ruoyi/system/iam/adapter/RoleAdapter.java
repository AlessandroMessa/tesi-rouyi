package com.ruoyi.system.iam.adapter;

import com.ruoyi.system.iam.domain.port.RolePort;
import org.springframework.stereotype.Component;

@Component
public class RoleAdapter implements RolePort {
    private final SysRoleMapper roleMapper;
    public RoleAdapter(SysRoleMapper roleMapper) { this.roleMapper = roleMapper; }
    @Override
    public boolean isDeptCheckStrictly(Long roleId) {
        return roleMapper.selectRoleById(roleId).isDeptCheckStrictly();
    }
}