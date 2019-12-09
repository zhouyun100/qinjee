package com.qinjee.masterdata.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * t_company_menu
 * @author
 */
@Data
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
    @DateTimeFormat(pattern = "yyyy-MM-dd" )//页面写入数据库时格式化
    @JSONField(format = "yyyy-MM-dd ")//数据库导出页面时json格式化
    private Date createTime;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd" )//页面写入数据库时格式化
    @JSONField(format = "yyyy-MM-dd ")//数据库导出页面时json格式化
    private Date updateTime;

    /**
     * 是否删除
     */
    private Short isDelete;

    private static final long serialVersionUID = 1L;
}
