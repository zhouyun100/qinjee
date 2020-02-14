package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude
public class ContractParamVo implements Serializable {
    /**
     * 合同id
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

    private List <String> applicationScopeCode;
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

    private static final long serialVersionUID = 1L;
}
