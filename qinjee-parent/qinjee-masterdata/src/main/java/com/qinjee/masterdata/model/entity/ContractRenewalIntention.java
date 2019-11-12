package com.qinjee.masterdata.model.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * t_contract_renewal_intention
 * @author 
 */
@Data
public class ContractRenewalIntention implements Serializable {
    /**
     * 续签意向ID
     */
    private Integer renewalIntentionId;
    /**
     * 合同编号
     */
    private String contractNumber;
    /**
     * 意向状态(待确认，已确认)
     */
    private String intensionStatus;
    /**
     * 企业id
     */
    private Integer companyId;
    /**
     * 档案ID
     */
    @NotNull
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
     * 是否同意（1表示同意）
     */
    private Short isAgree;

    /**
     * 续签意见（对是否同意的一些补充）
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

}