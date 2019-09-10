package com.qinjee.masterdata.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_position_grade
 * @author
 */
public class PositionGrade implements Serializable {
    /**
     * 职等ID
     */
    private Integer positionGradeId;

    /**
     * 职等名称
     */
    private String positionGradeName;

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

    public Integer getPositionGradeId() {
        return positionGradeId;
    }

    public void setPositionGradeId(Integer positionGradeId) {
        this.positionGradeId = positionGradeId;
    }

    public String getPositionGradeName() {
        return positionGradeName;
    }

    public void setPositionGradeName(String positionGradeName) {
        this.positionGradeName = positionGradeName;
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
