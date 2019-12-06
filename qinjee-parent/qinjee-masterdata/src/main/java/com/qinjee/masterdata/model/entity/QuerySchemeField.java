package com.qinjee.masterdata.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_query_scheme_field
 * @author
 */
@Data
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


}
