package com.qinjee.masterdata.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_company_info
 * @author
 */
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

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusinessLicenseId() {
        return businessLicenseId;
    }

    public void setBusinessLicenseId(String businessLicenseId) {
        this.businessLicenseId = businessLicenseId;
    }

    public String getRegistAddress() {
        return registAddress;
    }

    public void setRegistAddress(String registAddress) {
        this.registAddress = registAddress;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getWelcomeTitle() {
        return welcomeTitle;
    }

    public void setWelcomeTitle(String welcomeTitle) {
        this.welcomeTitle = welcomeTitle;
    }

    public String getWelcomeContent() {
        return welcomeContent;
    }

    public void setWelcomeContent(String welcomeContent) {
        this.welcomeContent = welcomeContent;
    }

    public String getLogoImgUrl() {
        return logoImgUrl;
    }

    public void setLogoImgUrl(String logoImgUrl) {
        this.logoImgUrl = logoImgUrl;
    }

    public String getBackgroundImgUrl() {
        return backgroundImgUrl;
    }

    public void setBackgroundImgUrl(String backgroundImgUrl) {
        this.backgroundImgUrl = backgroundImgUrl;
    }

    public Integer getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(Integer userNumber) {
        this.userNumber = userNumber;
    }

    public Date getValidEndDate() {
        return validEndDate;
    }

    public void setValidEndDate(Date validEndDate) {
        this.validEndDate = validEndDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Short getIsSaas() {
        return isSaas;
    }

    public void setIsSaas(Short isSaas) {
        this.isSaas = isSaas;
    }

    public Short getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Short isEnable) {
        this.isEnable = isEnable;
    }
}
