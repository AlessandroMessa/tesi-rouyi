package com.ruoyi.system.iam.domain.port;

import com.ruoyi.system.iam.domain.Dept;
import java.util.List;
import java.util.Optional;

public interface DeptRepository {

    List<Dept> findByFilter(Dept filter);

    Optional<Dept> findById(Long id);

    int countNormalChildren(Long deptId);

    boolean hasChildren(Long deptId);

    boolean existsUserInDept(Long deptId);

    boolean isNameUnique(String name, Long parentId, Long selfId);

    int insert(Dept dept);

    int update(Dept dept);

    void updateAncestors(Long deptId, String newAncestorsCsv, String oldAncestorsCsv);

    int deleteById(Long deptId);

    List<Long> findIdsByRole(Long roleId, boolean strictly);

    void enableParents(Long... ids);

    List<Dept> findChildren(Long deptId);
}
