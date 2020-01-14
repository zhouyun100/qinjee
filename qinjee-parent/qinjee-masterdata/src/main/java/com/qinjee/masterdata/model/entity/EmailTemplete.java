package com.qinjee.masterdata.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * t_email_templete
 * @author
 */
public class EmailTemplete implements Serializable {
    /**
     * 模板ID
     */
    private Integer emailTempleteId;

    /**
     * 模板标题
     */
    private String emailTempleteTitle;

    /**
     * 模板正文
     */
    private String emailTempleteContent;

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
   // //@DateTimeFormat(pattern = "yyyy-MM-dd" )
    ////@JSONField(format = "yyyy-MM-dd ")
    private Date createTime;

    /**
     * 是否删除
     */
    private Short isDelete;

    private static final long serialVersionUID = 1L;

    public Integer getEmailTempleteId() {
        return emailTempleteId;
    }

    public void setEmailTempleteId(Integer emailTempleteId) {
        this.emailTempleteId = emailTempleteId;
    }

    public String getEmailTempleteTitle() {
        return emailTempleteTitle;
    }

    public void setEmailTempleteTitle(String emailTempleteTitle) {
        this.emailTempleteTitle = emailTempleteTitle;
    }

    public String getEmailTempleteContent() {
        return emailTempleteContent;
    }

    public void setEmailTempleteContent(String emailTempleteContent) {
        this.emailTempleteContent = emailTempleteContent;
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
