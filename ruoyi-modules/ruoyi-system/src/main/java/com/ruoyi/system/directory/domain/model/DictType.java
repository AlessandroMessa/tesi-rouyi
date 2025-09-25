package com.ruoyi.system.directory.domain.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Domain model per il dizionario: tipo (puro, senza dipendenze legacy)
 */
public class DictType {

    private Long dictId;

    @NotBlank
    @Size(max = 100)
    private String dictName;

    @NotBlank
    @Size(max = 100)
    @Pattern(regexp = "^[a-z][a-z0-9_]*$")
    private String dictType;

    private String createBy;
    private java.time.LocalDateTime createTime;
    private String updateBy;
    private java.time.LocalDateTime updateTime;

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    private String remark;

    /** Stato del tipo dizionario */
    private Status status = Status.ENABLED;

    public enum Status { ENABLED, DISABLED }

    // --- getters/setters ---
    public Long getDictId() { return dictId; }
    public void setDictId(Long dictId) { this.dictId = dictId; }

    public String getDictName() { return dictName; }
    public void setDictName(String dictName) { this.dictName = dictName; }

    public String getDictType() { return dictType; }
    public void setDictType(String dictType) { this.dictType = dictType; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    // equals/hashCode opzionali se usi Set/Map o test
    @Override
    public String toString() {
        return "DictType{" +
                "dictId=" + dictId +
                ", dictName='" + dictName + '\'' +
                ", dictType='" + dictType + '\'' +
                ", status=" + status +
                '}';
    }
}
