package com.ruoyi.system.dept.adapter;

import com.ruoyi.system.api.domain.SysDept;
import com.ruoyi.system.api.domain.SysRole;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.dept.domain.model.Dept;
import com.ruoyi.system.mapper.iam.user.SysRoleMapper;
import com.ruoyi.system.mapper.iam.user.SysUserMapper;
import com.ruoyi.system.dept.domain.port.DataScopePort;
import com.ruoyi.common.datascope.annotation.DataScope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataScopeAdapter implements DataScopePort {

    private final SysRoleMapper roleMapper;
    private final SysUserMapper userMapper;
    private final SysDeptMapper deptMapper;

    public DataScopeAdapter(SysRoleMapper roleMapper, SysUserMapper userMapper, SysDeptMapper deptMapper) {
        this.roleMapper = roleMapper;
        this.userMapper = userMapper;
        this.deptMapper = deptMapper;
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
    @Override
    @DataScope(deptAlias = "d")
    public List<Dept> selectDeptListWithScope(Dept filter) {
        SysDept dto = SysDeptAdapter.toSysDept(filter);
        List<SysDept> rows = deptMapper.selectDeptList(dto);
        return rows.stream()
                .map(SysDeptAdapter::toDomain)
                .collect(Collectors.toList());
    }
}
