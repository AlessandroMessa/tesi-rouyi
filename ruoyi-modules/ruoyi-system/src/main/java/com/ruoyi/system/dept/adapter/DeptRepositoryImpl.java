package com.ruoyi.system.dept.adapter;

import com.ruoyi.system.api.domain.SysDept;
import com.ruoyi.system.dept.domain.model.Dept;
import com.ruoyi.system.dept.domain.port.DeptRepository;
import com.ruoyi.system.domain.port.SecurityPort;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DeptRepositoryImpl implements DeptRepository {

    private final SysDeptMapper deptMapper;
    private final SecurityPort securityPort; // opzionale, per audit

    public DeptRepositoryImpl(SysDeptMapper deptMapper, SecurityPort securityPort) {
        this.deptMapper = deptMapper;
        this.securityPort = securityPort;
    }

    @Override
    public List<Dept> findByFilter(Dept filter) {
        SysDept q = SysDeptAdapter.toSysDept(filter);
        List<SysDept> rows = deptMapper.selectDeptList(q);
        return rows == null ? new ArrayList<>() :
                rows.stream().map(SysDeptAdapter::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Dept> findById(Long id) {
        SysDept row = deptMapper.selectDeptById(id);
        return row == null ? Optional.empty() : Optional.of(SysDeptAdapter.toDomain(row));
    }

    @Override
    public int countNormalChildren(Long deptId) {
        return deptMapper.selectNormalChildrenDeptById(deptId);
    }

    @Override
    public boolean hasChildren(Long deptId) {
        return deptMapper.hasChildByDeptId(deptId) > 0;
    }

    @Override
    public boolean existsUserInDept(Long deptId) {
        return deptMapper.checkDeptExistUser(deptId) > 0;
    }

    @Override
    public boolean isNameUnique(String name, Long parentId, Long selfId) {
        SysDept info = deptMapper.checkDeptNameUnique(name, parentId);
        if (info == null) return true; // nessun duplicato
        Long foundId = info.getDeptId();
        // unico se il duplicato trovato è “me stesso”
        return foundId != null && foundId.equals(selfId);
    }

    @Override
    public int insert(Dept dept) {
        SysDept po = SysDeptAdapter.toSysDept(dept);
        // Audit: imposta createBy qui in adapter (non nel dominio)
        String username = safeUsername();
        if (username != null) {
            po.setCreateBy(username);
        }
        return deptMapper.insertDept(po);
    }

    @Override
    public int update(Dept dept) {
        SysDept po = SysDeptAdapter.toSysDept(dept);
        String username = safeUsername();
        if (username != null) {
            po.setUpdateBy(username);
        }
        return deptMapper.updateDept(po);
    }

    @Override
    public void updateAncestors(Long deptId, String newAncestorsCsv, String oldAncestorsCsv) {
        // Replica 1:1 della vecchia logica: ricalcola gli ancestors per i figli e salva in bulk
        List<SysDept> children = deptMapper.selectChildrenDeptById(deptId);
        if (children == null || children.isEmpty()) return;
        for (SysDept child : children) {
            String anc = child.getAncestors();
            if (anc != null) {
                child.setAncestors(anc.replaceFirst(oldAncestorsCsv, newAncestorsCsv));
            }
        }
        deptMapper.updateDeptChildren(children);
    }

    @Override
    public int deleteById(Long deptId) {
        return deptMapper.deleteDeptById(deptId);
    }

    @Override
    public List<Long> findIdsByRole(Long roleId, boolean strictly) {
        return deptMapper.selectDeptListByRoleId(roleId, strictly);
    }

    @Override
    public void enableParents(Long... ids) {
        if (ids == null || ids.length == 0) return;
        Long[] arr = new Long[ids.length];
        System.arraycopy(ids, 0, arr, 0, ids.length);
        deptMapper.updateDeptStatusNormal(arr);
    }

    @Override
    public List<Dept> findChildren(Long deptId) {
        List<SysDept> rows = deptMapper.selectChildrenDeptById(deptId);
        return rows == null ? new ArrayList<>() :
                rows.stream().map(SysDeptAdapter::toDomain).collect(Collectors.toList());
    }

    /** Recupera lo username corrente in modo sicuro senza portarlo nel dominio. */
    private String safeUsername() {
        try {
            // preferisci SecurityPort se espone getCurrentUsername()
            if (securityPort != null) {
                try {
                    // se esiste nel tuo SecurityPort
                    java.lang.reflect.Method m = securityPort.getClass().getMethod("getCurrentUsername");
                    Object v = m.invoke(securityPort);
                    if (v instanceof String) return (String) v;
                } catch (NoSuchMethodException ignore) { /* fallback sotto */ }
            }
            // fallback lecito nell'adapter: dipendenza infra
            return SecurityUtils.getUsername();
        } catch (Exception e) {
            return null;
        }
    }
}

