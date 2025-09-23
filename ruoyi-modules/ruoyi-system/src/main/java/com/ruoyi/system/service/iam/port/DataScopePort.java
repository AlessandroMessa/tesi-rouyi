package com.ruoyi.system.service.iam.port;

import com.ruoyi.system.api.domain.SysDept;
import com.ruoyi.system.api.domain.SysRole;
import com.ruoyi.system.api.domain.SysUser;

import java.util.List;

public interface DataScopePort {
    List<SysRole> selectRoleList(com.ruoyi.system.api.domain.SysRole role);
    List<SysUser> selectUserListWithScope(SysUser user);
    List<SysUser> selectAllocatedListWithScope(SysUser user);
    List<SysUser> selectUnallocatedListWithScope(SysUser user);
    List<SysDept> selectDeptListWithScope(SysDept dept);
}