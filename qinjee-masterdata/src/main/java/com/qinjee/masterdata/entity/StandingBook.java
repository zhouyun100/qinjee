package com.qinjee.masterdata.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_standing_book
 * @author 
 */
public class StandingBook implements Serializable {
    /**
     * 台账ID
     */
    private Integer standingBookId;

    /**
     * 台账名称
     */
    private String standingBookName;

    /**
     * 台账备注
     */
    private String standingBookRemark;

    /**
     * 台账描述
     */
    private String standingBookDescribe;

    /**
     * 企业ID
     */
    private Integer companyId;

    /**
     * 档案ID
     */
    private Integer archiveId;

    /**
     * 是否共享
     */
    private Short isShare;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否启用
     */
    private Short isEnable;

    /**
     * 操作人ID
     */
    private Integer creatorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Short isDelete;

    private static final long serialVersionUID = 1L;

    public Integer getStandingBookId() {
        return standingBookId;
    }

    public void setStandingBookId(Integer standingBookId) {
        this.standingBookId = standingBookId;
    }

    public String getStandingBookName() {
        return standingBookName;
    }

    public void setStandingBookName(String standingBookName) {
        this.standingBookName = standingBookName;
    }

    public String getStandingBookRemark() {
        return standingBookRemark;
    }

    public void setStandingBookRemark(String standingBookRemark) {
        this.standingBookRemark = standingBookRemark;
    }

    public String getStandingBookDescribe() {
        return standingBookDescribe;
    }

    public void setStandingBookDescribe(String standingBookDescribe) {
        this.standingBookDescribe = standingBookDescribe;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(Integer archiveId) {
        this.archiveId = archiveId;
    }

    public Short getIsShare() {
        return isShare;
    }

    public void setIsShare(Short isShare) {
        this.isShare = isShare;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Short getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Short isEnable) {
        this.isEnable = isEnable;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Short getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Short isDelete) {
        this.isDelete = isDelete;
    }
}