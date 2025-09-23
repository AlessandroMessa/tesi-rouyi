package com.ruoyi.system.service.iam.port;

import com.ruoyi.system.api.domain.SysRole;
import java.util.List;

public interface DataScopePort {
    List<SysRole> selectRoleList(com.ruoyi.system.api.domain.SysRole role);
}