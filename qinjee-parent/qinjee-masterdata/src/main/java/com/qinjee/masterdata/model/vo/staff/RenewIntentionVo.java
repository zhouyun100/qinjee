package com.qinjee.masterdata.model.vo.staff;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
@Data
@JsonInclude
public class RenewIntentionVo implements Serializable {
    private  String userName;
    private  String employeeNumber;
    private  String businessUnitName;
    private  String orgName;
    private  String postName;
    private  Date sendTime;
    private  String renewalOpinion;
    private  String sendUser;
    private  Integer archiveId;
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
     * 合同开始日期
     */
   // @DateTimeFormat(pattern = "yyyy-MM-dd" )
    //@JSONField(format = "yyyy-MM-dd ")
    private Date contractBeginDate;

    /**
     * 合同结束日期
     */
   // @DateTimeFormat(pattern = "yyyy-MM-dd" )
    //@JSONField(format = "yyyy-MM-dd ")
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

    private Date updateTime;
    private Date createTime;
}
