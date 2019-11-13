package com.qinjee.masterdata.model.vo.staff;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
/**
 * @author Administrator
 */
@Data
public class CreatNumberVo implements Serializable {
    /**
     *规则前缀
     */
    @NotNull
    private String  RulePrefix;
    /**
     *日期规则
     */
    @NotNull
    private String dateRule;
    /**
     *合同规则中缀
     */
    @NotNull
    private String  RuleInfix;
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
