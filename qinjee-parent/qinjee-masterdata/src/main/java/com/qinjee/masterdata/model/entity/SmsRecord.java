package com.qinjee.masterdata.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * t_sms_record
 * @author
 */
public class SmsRecord implements Serializable {
    /**
     * 短信记录ID
     */
    private Integer smsRecordId;

    /**
     * 短信配置ID
     */
    private Integer smsConfigId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 国家代码
     */
    private String nationCode;

    /**
     * 发送内容
     */
    private String sendMsg;

    /**
     * 发送时间
     */
   // @DateTimeFormat(pattern = "yyyy-MM-dd" )
    //@JSONField(format = "yyyy-MM-dd ")
    private Date sendTime;

    /**
     * 接收时间
     */
   // @DateTimeFormat(pattern = "yyyy-MM-dd" )
    //@JSONField(format = "yyyy-MM-dd ")
    private Date userReceiveTime;

    /**
     * 报告状态
     */
    private String reportStatus;

    /**
     * 错误信息
     */
    private String errmsg;

    /**
     * 描述
     */
    private String description;

    /**
     * SID
     */
    private String sid;

    /**
     * 企业ID
     */
    private Integer companyId;

    private static final long serialVersionUID = 1L;

    public Integer getSmsRecordId() {
        return smsRecordId;
    }

    public void setSmsRecordId(Integer smsRecordId) {
        this.smsRecordId = smsRecordId;
    }

    public Integer getSmsConfigId() {
        return smsConfigId;
    }

    public void setSmsConfigId(Integer smsConfigId) {
        this.smsConfigId = smsConfigId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNationCode() {
        return nationCode;
    }

    public void setNationCode(String nationCode) {
        this.nationCode = nationCode;
    }

    public String getSendMsg() {
        return sendMsg;
    }

    public void setSendMsg(String sendMsg) {
        this.sendMsg = sendMsg;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getUserReceiveTime() {
        return userReceiveTime;
    }

    public void setUserReceiveTime(Date userReceiveTime) {
        this.userReceiveTime = userReceiveTime;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
