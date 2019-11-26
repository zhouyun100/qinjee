package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
/**
 * @author Administrator
 */
@Data
@JsonInclude
public class CreatNumberVo implements Serializable {
    /**
     *合同规则前缀
     */
    private String  contractRulePrefix;
    /**
     *合同规则中缀
     */
    private String  contractRuleInfix;
    /**
     *合同规则后缀
     */
    private String  contractRuleSuffix;
    /**
     *规则前缀
     */
    private String  employeeNumberPrefix;
    /**
     *规则中缀
     */
    private String  employeeNumberInfix;
    /**
     * 工号后缀
     */
    @ApiModelProperty("工号后缀")
    private String employeeNumberSuffix;

    /**
     *日期规则
     */
    private String dateRule;

    /**
     *流水号位数
     */
    @NotNull
    private Short   digitCapacity;

    private static final long serialVersionUID = 1L;
}
