package com.qinjee.masterdata.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_query_scheme_sort
 * @author
 */
public class QuerySchemeSort implements Serializable {
    /**
     * 方案排序表主键
     */
    private Integer querySchemeSortId;

    /**
     * 查询方案ID
     */
    private Integer querySchemeId;

    /**
     * 字段ID
     */
    private Integer fieldId;

    /**
     * 排序规则(升/降)
     */
    private String orderByRule;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Integer getQuerySchemeSortId() {
        return querySchemeSortId;
    }

    public void setQuerySchemeSortId(Integer querySchemeSortId) {
        this.querySchemeSortId = querySchemeSortId;
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

    public String getOrderByRule() {
        return orderByRule;
    }

    public void setOrderByRule(String orderByRule) {
        this.orderByRule = orderByRule;
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
