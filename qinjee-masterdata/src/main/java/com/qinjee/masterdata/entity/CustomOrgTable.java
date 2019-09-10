package com.qinjee.masterdata.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_custom_org_table
 * @author 
 */
public class CustomOrgTable implements Serializable {
    /**
     * 表ID
     */
    private Integer tableId;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 企业ID
     */
    private Integer companyId;

    /**
     * 是否系统定义
     */
    private Short isSystemDefine;

    /**
     * 是否启用
     */
    private Short isEnable;

    /**
     * 排序
     */
    private Integer sort;

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

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Short getIsSystemDefine() {
        return isSystemDefine;
    }

    public void setIsSystemDefine(Short isSystemDefine) {
        this.isSystemDefine = isSystemDefine;
    }

    public Short getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Short isEnable) {
        this.isEnable = isEnable;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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