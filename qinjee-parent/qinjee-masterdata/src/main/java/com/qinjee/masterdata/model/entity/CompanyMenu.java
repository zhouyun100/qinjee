package com.qinjee.masterdata.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_company_menu
 * @author
 */
@Data
@JsonInclude
public class CompanyMenu implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 企业ID
     */
    private Integer companyId;

    /**
     * 菜单ID
     */
    private Integer menuId;

    /**
     * 菜单名称
     */
    private Integer menuName;

    /**
     * 图标地址
     */
    private String iconUrl;

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
