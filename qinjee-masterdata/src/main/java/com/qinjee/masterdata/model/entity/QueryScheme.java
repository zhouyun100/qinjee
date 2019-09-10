package com.qinjee.masterdata.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_query_scheme
 * @author
 */
public class QueryScheme implements Serializable {
    /**
     * 查询方案ID
     */
    private Integer querySchemeId;

    /**
     * 查询方案名称
     */
    private String querySchemeName;

    /**
     * 档案ID
     */
    private Integer archiveId;

    /**
     * 排序
     */
    private Integer sort;

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

    public Integer getQuerySchemeId() {
        return querySchemeId;
    }

    public void setQuerySchemeId(Integer querySchemeId) {
        this.querySchemeId = querySchemeId;
    }

    public String getQuerySchemeName() {
        return querySchemeName;
    }

    public void setQuerySchemeName(String querySchemeName) {
        this.querySchemeName = querySchemeName;
    }

    public Integer getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(Integer archiveId) {
        this.archiveId = archiveId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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
