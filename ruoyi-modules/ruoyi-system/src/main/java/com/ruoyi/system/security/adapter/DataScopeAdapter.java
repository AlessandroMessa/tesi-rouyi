package com.ruoyi.system.security.adapter;

import com.ruoyi.system.api.domain.SysRole;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.iam.port.DataScopePort;
import com.ruoyi.common.datascope.annotation.DataScope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataScopeAdapter implements DataScopePort {

    private final SysRoleMapper roleMapper;
    private final SysUserMapper userMapper;

    public DataScopeAdapter(SysRoleMapper roleMapper, SysUserMapper userMapper) {
        this.roleMapper = roleMapper;
        this.userMapper = userMapper;
    }

    @Override
    @DataScope(deptAlias = "d")
    public List<SysRole> selectRoleList(SysRole role) {
        return roleMapper.selectRoleList(role);
    }
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserListWithScope(SysUser user) {
        return userMapper.selectUserList(user);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectAllocatedListWithScope(SysUser user) {
        return userMapper.selectAllocatedList(user);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUnallocatedListWithScope(SysUser user) {
        return userMapper.selectUnallocatedList(user);
    }
}
