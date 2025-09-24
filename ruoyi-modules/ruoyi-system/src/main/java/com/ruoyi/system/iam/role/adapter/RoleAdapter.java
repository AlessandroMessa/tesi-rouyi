package com.ruoyi.system.iam.role.adapter;

import com.ruoyi.system.iam.role.domain.port.RolePort;
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