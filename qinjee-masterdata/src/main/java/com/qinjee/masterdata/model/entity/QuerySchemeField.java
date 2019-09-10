package com.qinjee.masterdata.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_query_scheme_field
 * @author
 */
public class QuerySchemeField implements Serializable {
    /**
     * 方案字段表主键
     */
    private Integer querySchemeFieldId;

    /**
     * 查询方案ID
     */
    private Integer querySchemeId;

    /**
     * 字段ID
     */
    private Integer fieldId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Integer getQuerySchemeFieldId() {
        return querySchemeFieldId;
    }

    public void setQuerySchemeFieldId(Integer querySchemeFieldId) {
        this.querySchemeFieldId = querySchemeFieldId;
    }

    public Integer getQuerySchemeId() {
        return querySchemeId;
    }

    public void setQuerySchemeId(Integer querySchemeId) {
        this.querySchemeId = querySchemeId;
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
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
}
