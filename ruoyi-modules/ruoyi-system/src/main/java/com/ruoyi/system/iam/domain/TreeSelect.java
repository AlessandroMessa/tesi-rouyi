package com.ruoyi.system.iam.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.List;

public class TreeSelect implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String label;
    private boolean disabled;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelect> children;

    public TreeSelect() { }

    public TreeSelect(Long id, String label, boolean disabled, List<TreeSelect> children) {
        this.id = id;
        this.label = label;
        this.disabled = disabled;
        this.children = children;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public boolean isDisabled() { return disabled; }
    public void setDisabled(boolean disabled) { this.disabled = disabled; }

    public List<TreeSelect> getChildren() { return children; }
    public void setChildren(List<TreeSelect> children) { this.children = children; }
}
