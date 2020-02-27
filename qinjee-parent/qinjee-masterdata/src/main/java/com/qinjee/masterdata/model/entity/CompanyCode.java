package com.qinjee.masterdata.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_company_code
 * @author
 */
@Data
@JsonInclude
public class CompanyCode implements Serializable {
    /**
     * 代码ID
     */
    private Integer codeId;

    /**
     * 代码名称
     */
    private String codeName;

    /**
     * 代码父级ID
     */
    private Integer codeParentId;

    /**
     * 是否系统定义
     */
    private Short isSystemDefine;

    /**
     * 企业ID
     */
    private Integer companyId;

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
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Short isDelete;

    private static final long serialVersionUID = 1L;
}
