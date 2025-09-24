package com.ruoyi.system.service.iam.dept;

import java.util.List;
import com.ruoyi.system.domain.iam.dept.Dept;
import com.ruoyi.system.domain.iam.dept.TreeSelect;

/**
 * 部门管理 服务层 (domain-based)
 */
public interface ISysDeptService {

    /** 查询部门管理数据 */
    List<Dept> selectDeptList(Dept dept);

    /** 查询部门树结构信息 */
    List<TreeSelect> selectDeptTreeList(Dept dept);

    /** 构建前端所需要树结构 */
    List<Dept> buildDeptTree(List<Dept> depts);

    /** 构建前端所需要下拉树结构 */
    List<TreeSelect> buildDeptTreeSelect(List<Dept> depts);

    /** 根据角色ID查询部门树信息 */
    List<Long> selectDeptListByRoleId(Long roleId);

    /** 根据部门ID查询信息 */
    Dept selectDeptById(Long deptId);

    /** 根据ID查询所有子部门（正常状态） */
    int selectNormalChildrenDeptById(Long deptId);

    /** 是否存在部门子节点 */
    boolean hasChildByDeptId(Long deptId);

    /** 查询部门是否存在用户 */
    boolean checkDeptExistUser(Long deptId);

    /** 校验部门名称是否唯一 */
    boolean checkDeptNameUnique(Dept dept);

    /** 校验部门是否有数据权限 */
    void checkDeptDataScope(Long deptId);

    /** 新增保存部门信息 */
    int insertDept(Dept dept);

    /** 修改保存部门信息 */
    int updateDept(Dept dept);

    /** 删除部门管理信息 */
    int deleteDeptById(Long deptId);
}
