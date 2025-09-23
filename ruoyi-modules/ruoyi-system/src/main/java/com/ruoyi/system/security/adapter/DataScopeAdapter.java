package com.ruoyi.system.security.adapter;

import com.ruoyi.system.api.domain.SysRole;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.service.iam.port.DataScopePort;
import com.ruoyi.common.datascope.annotation.DataScope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataScopeAdapter implements DataScopePort {

    private final SysRoleMapper roleMapper;

    public DataScopeAdapter(SysRoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    @DataScope(deptAlias = "d")
    public List<SysRole> selectRoleList(SysRole role) {
        return roleMapper.selectRoleList(role);
    }
}
