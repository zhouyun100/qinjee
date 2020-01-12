package com.qinjee.masterdata.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * t_user_company
 * @author
 */
public class UserCompany implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 企业ID
     */
    private Integer companyId;

    /**
     * 是否启用
     */
    private Short isEnable;

    /**
     * 创建时间
     */
   // @DateTimeFormat(pattern = "yyyy-MM-dd" )
    //@JSONField(format = "yyyy-MM-dd ")
    private Date createTime;

    /**
     * 修改时间
     */
   // @DateTimeFormat(pattern = "yyyy-MM-dd" )
    //@JSONField(format = "yyyy-MM-dd ")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Short getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Short isEnable) {
        this.isEnable = isEnable;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
