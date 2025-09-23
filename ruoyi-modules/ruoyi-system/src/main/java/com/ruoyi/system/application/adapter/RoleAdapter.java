package com.ruoyi.system.application.adapter;

import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.service.iam.port.RolePort;
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