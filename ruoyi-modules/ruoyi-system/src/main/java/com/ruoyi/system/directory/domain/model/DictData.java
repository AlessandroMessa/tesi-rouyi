package com.ruoyi.system.directory.domain.model;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


public class DictData {

    private Long dictCode;
    private Long dictSort;
    @NotBlank @Size(max = 100)
    private String dictLabel;
    @NotBlank @Size(max = 100)
    private String dictValue;
    @NotBlank @Size(max = 100)
    private String dictType;
    @Size(max = 100)
    private String cssClass;
    private String listClass;


    private boolean isDefault;


    private Status status = Status.ENABLED;

    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
    private String remark;

    public enum Status { ENABLED, DISABLED }
    public Long getDictCode() { return dictCode; }
    public void setDictCode(Long dictCode) { this.dictCode = dictCode; }

    public Long getDictSort() { return dictSort; }
    public void setDictSort(Long dictSort) { this.dictSort = dictSort; }

    public String getDictLabel() { return dictLabel; }
    public void setDictLabel(String dictLabel) { this.dictLabel = dictLabel; }

    public String getDictValue() { return dictValue; }
    public void setDictValue(String dictValue) { this.dictValue = dictValue; }

    public String getDictType() { return dictType; }
    public void setDictType(String dictType) { this.dictType = dictType; }

    public String getCssClass() { return cssClass; }
    public void setCssClass(String cssClass) { this.cssClass = cssClass; }

    public String getListClass() { return listClass; }
    public void setListClass(String listClass) { this.listClass = listClass; }

    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean aDefault) { isDefault = aDefault; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
