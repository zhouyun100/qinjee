package com.qinjee.masterdata.model.vo.staff;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class ContractParamVo implements Serializable {
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
    @NotNull
    private String  applicationScopeCode;
    /**
     *提前提醒参数
     */
    @NotNull
    private Short   rememberDays;
    /**
     *合同规则前缀
     */
    @NotNull
    private String  contractRulePrefix;
    /**
     *日期规则
     */
    @NotNull
    private String dateRule;
    /**
     *合同规则中缀
     */
    @NotNull
    private String  contractRuleInfix;
    /**
     *流水号位数
     */
    @NotNull
    private Short   digitCapacity;
    /**
     *合同规则后缀
     */
    @NotNull
    private String  contractRuleSuffix;

    private static final long serialVersionUID = 1L;
}
