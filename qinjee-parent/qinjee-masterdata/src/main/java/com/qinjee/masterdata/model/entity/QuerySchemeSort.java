package com.qinjee.masterdata.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_query_scheme_sort
 * @author
 */
@JsonInclude
@Data
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
}
