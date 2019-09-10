package com.qinjee.masterdata.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_position_level
 * @author 
 */
public class PositionLevel implements Serializable {
    /**
     * 职级ID
     */
    private Integer positionLevelId;

    /**
     * 职级名称
     */
    private String positionLevelName;

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

    public Integer getPositionLevelId() {
        return positionLevelId;
    }

    public void setPositionLevelId(Integer positionLevelId) {
        this.positionLevelId = positionLevelId;
    }

    public String getPositionLevelName() {
        return positionLevelName;
    }

    public void setPositionLevelName(String positionLevelName) {
        this.positionLevelName = positionLevelName;
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