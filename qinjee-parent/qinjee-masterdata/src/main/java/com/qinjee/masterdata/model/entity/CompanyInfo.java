package com.qinjee.masterdata.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_company_info
 * @author
 */
@Data
@JsonInclude
public class CompanyInfo implements Serializable {
    /**
     * 企业ID
     */
    private Integer companyId;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 企业类型
     */
    private String companyType;

    /**
     * 营业执照ID
     */
    private String businessLicenseId;

    /**
     * 注册地址
     */
    private String registAddress;

    /**
     * 办公地址
     */
    private String officeAddress;

    /**
     * 欢迎页标题
     */
    private String welcomeTitle;

    /**
     * 欢迎语
     */
    private String welcomeContent;

    /**
     * logo图路径
     */
    private String logoImgUrl;

    /**
     * 背景图路径
     */
    private String backgroundImgUrl;

    /**
     * 使用人数
     */
    private Integer userNumber;

    /**
     * 有效截止日期
     */
    private Date validEndDate;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否SaaS
     */
    private Short isSaas;

    /**
     * 是否启用
     */
    private Short isEnable;

    private static final long serialVersionUID = 1L;

}
