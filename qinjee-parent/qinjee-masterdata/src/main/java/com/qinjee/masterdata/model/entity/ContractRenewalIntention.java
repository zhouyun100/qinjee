package com.qinjee.masterdata.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_contract_renewal_intention
 * @author 
 */
public class ContractRenewalIntention implements Serializable {
    /**
     * 续签意向ID
     */
    private Integer renewalIntentionId;

    /**
     * 档案ID
     */
    private Integer archiveId;

    /**
     * 合同开始日期
     */
    private Date contractBeginDate;

    /**
     * 合同结束日期
     */
    private Date contractEndDate;

    /**
     * 合同期限(月)
     */
    private Integer contractPeriodMonth;

    /**
     * 合同期限类型
     */
    private String contractPeriodType;

    /**
     * 是否同意
     */
    private Short isAgree;

    /**
     * 续签意见
     */
    private String renewalOpinion;

    /**
     * 操作人ID
     */
    private Integer operatorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getRenewalIntentionId() {
        return renewalIntentionId;
    }

    public void setRenewalIntentionId(Integer renewalIntentionId) {
        this.renewalIntentionId = renewalIntentionId;
    }

    public Integer getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(Integer archiveId) {
        this.archiveId = archiveId;
    }

    public Date getContractBeginDate() {
        return contractBeginDate;
    }

    public void setContractBeginDate(Date contractBeginDate) {
        this.contractBeginDate = contractBeginDate;
    }

    public Date getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Date contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public Integer getContractPeriodMonth() {
        return contractPeriodMonth;
    }

    public void setContractPeriodMonth(Integer contractPeriodMonth) {
        this.contractPeriodMonth = contractPeriodMonth;
    }

    public String getContractPeriodType() {
        return contractPeriodType;
    }

    public void setContractPeriodType(String contractPeriodType) {
        this.contractPeriodType = contractPeriodType;
    }

    public Short getIsAgree() {
        return isAgree;
    }

    public void setIsAgree(Short isAgree) {
        this.isAgree = isAgree;
    }

    public String getRenewalOpinion() {
        return renewalOpinion;
    }

    public void setRenewalOpinion(String renewalOpinion) {
        this.renewalOpinion = renewalOpinion;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}