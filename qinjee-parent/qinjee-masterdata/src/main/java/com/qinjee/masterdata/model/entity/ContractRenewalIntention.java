package com.qinjee.masterdata.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * t_contract_renewal_intention
 * @author 
 */
@Data
@JsonInclude
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
    private String intentionStatus;
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
    @DateTimeFormat(pattern = "yyyy-MM-dd" )//页面写入数据库时格式化
    @JSONField(format = "yyyy-MM-dd ")//数据库导出页面时json格式化
    private Date createTime;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd" )//页面写入数据库时格式化
    @JSONField(format = "yyyy-MM-dd ")//数据库导出页面时json格式化
    private Date updateTime;

    private static final long serialVersionUID = 1L;

}