package com.qinjee.masterdata.model.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * t_labor_contract
 * @author 
 */
@Data
@ToString
public class LaborContract implements Serializable {
    /**
     * 合同ID
     */
    private Integer contractId;

    /**
     * 档案ID
     */
    private Integer archiveId;
    /**
     * 机构id
     */
    private Integer businessUnitId;
    /**
     * 部门id
     */
    private Integer OrgId;
    /**
     * 岗位id
     */
    private Integer postId;

    /**
     * 合同签订日期
     */
    private Date contractSignDate;

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
     * 合同主体
     */
    private String contractSubject;

    /**
     * 合同编号
     */
    private String contractNumber;

    /**
     * 合同状态
     */
    private String contractState;

    /**
     * 合同备注
     */
    private String contractRemark;

    /**
     * 签订次数
     */
    private Integer signNumber;

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