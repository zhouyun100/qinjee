package com.qinjee.masterdata.model.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * t_query_scheme
 * @author
 */
@Data
public class QueryScheme implements Serializable {
    /**
     * 查询方案ID
     */
    private Integer querySchemeId;

    /**
     * 查询方案名称
     */
    @NotNull
    private String querySchemeName;

    /**
     * 档案ID
     */
    @NotNull
    private Integer archiveId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 企业id
     */
    private Integer isDefault;

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
    @NotNull
    private Short isDelete;

    private static final long serialVersionUID = 1L;


}
