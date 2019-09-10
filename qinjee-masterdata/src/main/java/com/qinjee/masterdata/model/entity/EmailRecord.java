package com.qinjee.masterdata.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_email_record
 * @author
 */
public class EmailRecord implements Serializable {
    /**
     * 邮件记录ID
     */
    private Integer emailRecordId;

    /**
     * 模板ID
     */
    private Integer emailTempleteId;

    /**
     * 邮件配置ID
     */
    private Integer emailConfigId;

    /**
     * 邮件标题
     */
    private String emailTitle;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 发件人
     */
    private String fromUser;

    /**
     * 收件人
     */
    private String toUser;

    /**
     * 抄送人
     */
    private String ccUser;

    /**
     * 操作人
     */
    private Integer operatorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 发送状态
     */
    private String sendStatus;

    /**
     * 邮件正文
     */
    private byte[] emailContent;

    private static final long serialVersionUID = 1L;

    public Integer getEmailRecordId() {
        return emailRecordId;
    }

    public void setEmailRecordId(Integer emailRecordId) {
        this.emailRecordId = emailRecordId;
    }

    public Integer getEmailTempleteId() {
        return emailTempleteId;
    }

    public void setEmailTempleteId(Integer emailTempleteId) {
        this.emailTempleteId = emailTempleteId;
    }

    public Integer getEmailConfigId() {
        return emailConfigId;
    }

    public void setEmailConfigId(Integer emailConfigId) {
        this.emailConfigId = emailConfigId;
    }

    public String getEmailTitle() {
        return emailTitle;
    }

    public void setEmailTitle(String emailTitle) {
        this.emailTitle = emailTitle;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getCcUser() {
        return ccUser;
    }

    public void setCcUser(String ccUser) {
        this.ccUser = ccUser;
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

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public byte[] getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(byte[] emailContent) {
        this.emailContent = emailContent;
    }
}
