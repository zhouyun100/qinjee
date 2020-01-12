package com.qinjee.masterdata.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * t_sms_config
 * @author
 */
public class SmsConfig implements Serializable {
    /**
     * 短信配置ID
     */
    private Integer smsConfigId;

    /**
     * APPID
     */
    private Integer appId;

    /**
     * APPKEY
     */
    private String appKey;

    /**
     * 签名
     */
    private String smsSign;

    /**
     * 模板ID
     */
    private Integer templateId;

    /**
     * 模板内容
     */
    private String templateMsg;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 操作人ID
     */
    private Integer operatorId;

    /**
     * 创建时间
     */
   // @DateTimeFormat(pattern = "yyyy-MM-dd" )
    //@JSONField(format = "yyyy-MM-dd ")
    private Date createTime;

    /**
     * 是否删除
     */
    private Short isDelete;

    private static final long serialVersionUID = 1L;

    public Integer getSmsConfigId() {
        return smsConfigId;
    }

    public void setSmsConfigId(Integer smsConfigId) {
        this.smsConfigId = smsConfigId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSmsSign() {
        return smsSign;
    }

    public void setSmsSign(String smsSign) {
        this.smsSign = smsSign;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getTemplateMsg() {
        return templateMsg;
    }

    public void setTemplateMsg(String templateMsg) {
        this.templateMsg = templateMsg;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Short getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Short isDelete) {
        this.isDelete = isDelete;
    }
}
