package com.ruoyi.system.dept.service.impl;

import com.ruoyi.common.core.constant.UserConstants;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.system.dept.factory.TreeSelectFactory;
import com.ruoyi.system.dept.domain.model.Dept;
import com.ruoyi.system.dept.domain.model.DeptStatus;
import com.ruoyi.system.dept.domain.port.DataScopePort;
import com.ruoyi.system.dept.domain.port.DeptRepository;
import com.ruoyi.system.iam.role.domain.port.RolePort;
import com.ruoyi.system.common.SecurityPort;
import com.ruoyi.system.dept.service.ISysDeptService;
import com.ruoyi.system.shared.vo.TreeSelect;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeptAppService implements ISysDeptService {

    private final DeptRepository deptRepo;
    private final SecurityPort securityPort;
    private final DataScopePort dataScopePort;
    private final RolePort rolePort;

    public DeptAppService(DeptRepository deptRepo,
                          SecurityPort securityPort,
                          DataScopePort dataScopePort,
                          RolePort rolePort) {
        this.deptRepo = deptRepo;
        this.securityPort = securityPort;
        this.dataScopePort = dataScopePort;
        this.rolePort = rolePort;
    }


    /** 查询部门管理数据 */
    @Override
    public List<Dept> selectDeptList(Dept filter) {
        return dataScopePort.selectDeptListWithScope(filter);
    }

    /** 查询部门树结构信息 */
    @Override
    public List<TreeSelect> selectDeptTreeList(Dept filter) {
        List<Dept> depts = selectDeptList(filter);
        return buildDeptTreeSelect(depts);
    }

    /** 构建前端所需要树结构 */
    @Override
    public List<Dept> buildDeptTree(List<Dept> depts) {
        List<Dept> returnList = new ArrayList<>();
        List<Long> tempList = depts.stream().map(Dept::getId).collect(Collectors.toList());
        for (Dept dept : depts) {
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }

    /** 构建前端所需要下拉树结构 */
    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<Dept> depts) {
        List<Dept> deptTrees = buildDeptTree(depts);
        return deptTrees.stream()
                .map(TreeSelectFactory::fromDomain)
                .collect(Collectors.toList());
    }

    /** 根据角色ID查询部门树信息 */
    @Override
    public List<Long> selectDeptListByRoleId(Long roleId) {
        boolean strictly = rolePort.isDeptCheckStrictly(roleId);
        return deptRepo.findIdsByRole(roleId, strictly);
    }

    /** 根据部门ID查询信息 */
    @Override
    public Dept selectDeptById(Long deptId) {
        return deptRepo.findById(deptId).orElse(null);
    }

    /** 根据ID查询所有子部门（正常状态） */
    @Override
    public int selectNormalChildrenDeptById(Long deptId) {
        return deptRepo.countNormalChildren(deptId);
    }

    /** 是否存在子节点 */
    @Override
    public boolean hasChildByDeptId(Long deptId) {
        return deptRepo.hasChildren(deptId);
    }

    /** 查询部门是否存在用户 */
    @Override
    public boolean checkDeptExistUser(Long deptId) {
        return deptRepo.existsUserInDept(deptId);
    }

    /** 校验部门名称是否唯一 */
    @Override
    public boolean checkDeptNameUnique(Dept dept) {
        Long deptId = StringUtils.isNull(dept.getId()) ? -1L : dept.getId();
        boolean unique = deptRepo.isNameUnique(dept.getName(), dept.getParentId(), deptId);
        return unique ? UserConstants.UNIQUE : UserConstants.NOT_UNIQUE;
    }

    /** 校验部门是否有数据权限 */
    @Override
    public void checkDeptDataScope(Long deptId) {
        if (!securityPort.isAdmin(securityPort.getCurrentUserId()) && StringUtils.isNotNull(deptId)) {
            Dept probe = Dept.createNew(deptId, null, null, null, null,
                    null, null, null, null, false, null);
            List<Dept> depts = selectDeptList(probe);
            if (StringUtils.isEmpty(depts)) {
                throw new ServiceException("没有权限访问部门数据！");
            }
        }
    }

    /** 新增保存部门信息 */
    @Override
    public int insertDept(Dept dept) {
        Dept parent = deptRepo.findById(dept.getParentId()).orElse(null);
        if (parent != null && parent.getStatus() != DeptStatus.NORMAL) {
            throw new ServiceException("部门停用，不允许新增");
        }
        String ancestorsCsv = parent == null ? "0" : parent.ancestorsCsv() + "," + parent.getId();
        Dept newDept = Dept.createNew(
                dept.getId(), dept.getParentId(), Dept.Ancestors.parse(ancestorsCsv),
                dept.getName(), dept.getOrderNum(), dept.getLeader(),
                dept.getPhone(), dept.getEmail(), dept.getStatus(),
                dept.isDeleted(), parent != null ? parent.getName() : null
        );
        return deptRepo.insert(newDept);
    }

    /** 修改保存部门信息 */
    @Override
    public int updateDept(Dept dept) {
        Dept newParent = deptRepo.findById(dept.getParentId()).orElse(null);
        Dept oldDept = deptRepo.findById(dept.getId()).orElse(null);
        if (newParent != null && oldDept != null) {
            String newAncestors = newParent.ancestorsCsv() + "," + newParent.getId();
            String oldAncestors = oldDept.ancestorsCsv();
            deptRepo.updateAncestors(dept.getId(), newAncestors, oldAncestors);
        }
        int result = deptRepo.update(dept);
        if (dept.getStatus() == DeptStatus.NORMAL
                && dept.getAncestors() != null
                && !dept.getAncestors().isEmpty()) {
            deptRepo.enableParents(dept.getAncestors().toArray(new Long[0]));
        }
        return result;
    }

    /** 删除部门管理信息 */
    @Override
    public int deleteDeptById(Long deptId) {
        return deptRepo.deleteById(deptId);
    }

    // -------- private helpers ----------

    /** 递归列表 */
    private void recursionFn(List<Dept> list, Dept t) {
        List<Dept> childList = getChildList(list, t);
        // aggiorna i children del dominio
        childList.forEach(t::addChild);
        for (Dept child : childList) {
            if (hasChild(list, child)) {
                recursionFn(list, child);
            }
        }
    }

    /** 得到子节点列表 */
    private List<Dept> getChildList(List<Dept> list, Dept t) {
        List<Dept> result = new ArrayList<>();
        Iterator<Dept> it = list.iterator();
        while (it.hasNext()) {
            Dept n = it.next();
            if (n.getParentId() != null && n.getParentId().equals(t.getId())) {
                result.add(n);
            }
        }
        return result;
    }

    /** 判断是否有子节点 */
    private boolean hasChild(List<Dept> list, Dept t) {
        return !getChildList(list, t).isEmpty();
    }
}
