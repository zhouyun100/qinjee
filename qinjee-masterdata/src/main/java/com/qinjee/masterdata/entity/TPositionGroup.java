package com.qinjee.masterdata.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_position_group
 * @author
 */
public class TPositionGroup implements Serializable {
    /**
     * 职位族ID
     */
    private Integer positionGroupId;

    /**
     * 职位族名称
     */
    private String positionGroupName;

    /**
     * 企业ID
     */
    private Integer companyId;

    /**
     * 排序ID
     */
    private Integer sortId;

    /**
     * 操作人ID
     */
    private Integer operatorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除
     */
    private Short isDelete;

    private static final long serialVersionUID = 1L;

    public Integer getPositionGroupId() {
        return positionGroupId;
    }

    public void setPositionGroupId(Integer positionGroupId) {
        this.positionGroupId = positionGroupId;
    }

    public String getPositionGroupName() {
        return positionGroupName;
    }

    public void setPositionGroupName(String positionGroupName) {
        this.positionGroupName = positionGroupName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getSortId() {
        return sortId;
    }

    public void setSortId(Integer sortId) {
        this.sortId = sortId;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Short getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Short isDelete) {
        this.isDelete = isDelete;
    }
}
