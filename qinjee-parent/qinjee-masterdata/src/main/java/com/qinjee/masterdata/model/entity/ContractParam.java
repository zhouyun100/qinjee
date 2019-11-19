package com.qinjee.masterdata.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/**
 * @author Administrator
 */
@Data
@JsonInclude
public class ContractParam implements Serializable {
    /**
     * 合同参数
     */
    private Integer contractParamId;
    /**
     * 合同参数名称
     */
    private String  contractParamName;
    /**
     *合同参数说明
     */
    private String  contractParamDescribe;
    /**
     *适用范围CODE
     */
    private String  applicationScopeCode;
    /**
     *提前提醒参数
     */
    private Short   rememberDays;
    /**
     *合同规则前缀
     */
    private String  contractRulePrefix;
    /**
     *日期规则
     */
    private String dateRule;
    /**
     *合同规则中缀
     */
    private String  contractRuleInfix;
    /**
     *流水号位数
     */
    private Short   digitCapacity;
    /**
     *合同规则后缀
     */
    private String  contractRuleSuffix;
    /**
     *企业id
     */
    private Integer companyId;
    /**
     *是否启用
     */
    private Short   isEnable;
    /**
     *操作人id
     */
    private Integer operatorId;
    /**
     *创建时间
     */
    private Date    createTime;
    /**
     *修改时间
     */
    private Date    updateTime;
    /**
     *是否删除
     */
    private Short   isDelete;

    private static final long serialVersionUID = 1L;
}
