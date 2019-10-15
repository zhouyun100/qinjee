package com.qinjee.masterdata.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_post_grade_relation
 * @author
 */
public class PostGradeRelation implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 岗位ID
     */
    private Integer postId;

    /**
     * 职等ID
     */
    private Integer positionGradeId;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getPositionGradeId() {
        return positionGradeId;
    }

    public void setPositionGradeId(Integer positionGradeId) {
        this.positionGradeId = positionGradeId;
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
