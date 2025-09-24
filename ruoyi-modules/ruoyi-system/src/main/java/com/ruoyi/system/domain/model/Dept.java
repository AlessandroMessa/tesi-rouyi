package com.ruoyi.system.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Dept {

    private Long id;                // deptId
    private Long parentId;          // nullable for root
    private List<Long> ancestors;   // list of ancestor IDs (root to parent)
    private String name;            // deptName
    private Integer orderNum;       // sort order
    private String leader;          // optional
    private String phone;           // optional, no validation here
    private String email;           // optional, no validation here
    private DeptStatus status;      // NORMAL/DISABLED
    private boolean deleted;        // soft delete flag
    private String parentName;      // optional, convenience (not authoritative)
    private final List<Dept> children = new ArrayList<>();

    // --------- Constructors / factories ---------
    public Dept() { }

    public static Dept createNew(Long id,
                                 Long parentId,
                                 List<Long> ancestors,
                                 String name,
                                 Integer orderNum,
                                 String leader,
                                 String phone,
                                 String email,
                                 DeptStatus status,
                                 boolean deleted,
                                 String parentName) {
        Dept d = new Dept();
        d.id = id;
        d.parentId = parentId;
        d.ancestors = ancestors == null ? new ArrayList<>() : new ArrayList<>(ancestors);
        d.name = name;
        d.orderNum = orderNum;
        d.leader = leader;
        d.phone = phone;
        d.email = email;
        d.status = status == null ? DeptStatus.NORMAL : status;
        d.deleted = deleted;
        d.parentName = parentName;
        return d;
    }

    // Utility factory when you still get the comma-separated path from adapters
    public static Dept fromAncestorsString(Long id,
                                           Long parentId,
                                           String ancestorsCsv,
                                           String name,
                                           Integer orderNum,
                                           String leader,
                                           String phone,
                                           String email,
                                           DeptStatus status,
                                           boolean deleted,
                                           String parentName) {
        List<Long> list = Ancestors.parse(ancestorsCsv);
        return createNew(id, parentId, list, name, orderNum, leader, phone, email, status, deleted, parentName);
    }

    // --------- Domain behavior ---------
    public boolean isRoot() { return parentId == null || parentId == 0L; }

    public void addChild(Dept child) {
        if (child == null) return;
        child.parentId = this.id;
        child.ancestors = new ArrayList<>(this.ancestors);
        child.ancestors.add(this.id);
        this.children.add(child);
    }

    /** Rebase the subtree under a new parent, updating ancestors path. */
    public void reparentTo(Dept newParent) {
        Long oldParentId = this.parentId;
        List<Long> oldAncestors = new ArrayList<>(this.ancestors);

        this.parentId = (newParent == null ? 0L : newParent.id);
        this.ancestors = (newParent == null)
                ? new ArrayList<>()
                : concat(newParent.ancestors, newParent.id);

        // propagate to children
        for (Dept c : children) {
            c.replaceAncestorPrefix(oldAncestors, this.ancestors);
        }
    }

    private void replaceAncestorPrefix(List<Long> oldPrefix, List<Long> newPrefix) {
        if (ancestors.size() >= oldPrefix.size() && ancestors.subList(0, oldPrefix.size()).equals(oldPrefix)) {
            List<Long> tail = ancestors.subList(oldPrefix.size(), ancestors.size());
            this.ancestors = new ArrayList<>(newPrefix);
            this.ancestors.addAll(tail);
        }
        for (Dept c : children) {
            c.replaceAncestorPrefix(oldPrefix, newPrefix);
        }
    }

    private static List<Long> concat(List<Long> list, Long last) {
        List<Long> r = new ArrayList<>(list);
        r.add(last);
        return r;
    }

    // --------- Getters / minimal setters (no external frameworks) ---------
    public Long getId() { return id; }
    public Long getParentId() { return parentId; }
    public List<Long> getAncestors() { return Collections.unmodifiableList(ancestors); }
    public String getName() { return name; }
    public Integer getOrderNum() { return orderNum; }
    public String getLeader() { return leader; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public DeptStatus getStatus() { return status; }
    public boolean isDeleted() { return deleted; }
    public String getParentName() { return parentName; }
    public List<Dept> getChildren() { return Collections.unmodifiableList(children); }

    public void setStatus(DeptStatus status) { this.status = status; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    // Convenience for adapters that still need CSV
    public String ancestorsCsv() { return Ancestors.toCsv(ancestors); }

    // Equality by id (aggregate identity)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dept)) return false;
        Dept dept = (Dept) o;
        return Objects.equals(id, dept.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Dept{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", ancestors=" + ancestors +
                ", name='" + name + '\'' +
                ", orderNum=" + orderNum +
                ", status=" + status +
                ", deleted=" + deleted +
                '}';
    }

    // --------- tiny helper for CSV ancestors (kept internal) ---------
    public static final class Ancestors {
        public static List<Long> parse(String csv) {
            List<Long> list = new ArrayList<>();
            if (csv == null || csv.isEmpty()) return list;
            for (String s : csv.split(",")) {
                String t = s.trim();
                if (!t.isEmpty() && !"0".equals(t)) {
                    try { list.add(Long.parseLong(t)); } catch (NumberFormatException ignored) { }
                }
            }
            return list;
        }
        static String toCsv(List<Long> ids) {
            if (ids == null || ids.isEmpty()) return "0";
            StringBuilder sb = new StringBuilder("0");
            for (Long id : ids) sb.append(',').append(id);
            return sb.toString();
        }
    }
}
