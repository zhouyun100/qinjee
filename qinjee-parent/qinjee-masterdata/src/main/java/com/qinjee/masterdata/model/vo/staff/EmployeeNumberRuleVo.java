package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月25日 09:51:00
 */
@Data
@NoArgsConstructor
@JsonInclude
@ApiModel(description = "工号规则表Vo类")
public class EmployeeNumberRuleVo {

    /**
     * 工号前缀
     */
    @ApiModelProperty("工号前缀")
    private String employeeNumberPrefix;

    /**
     * 日期规则
     */
    @ApiModelProperty("日期规则")
    private String dateRule;

    /**
     * 工号中缀
     */
    @ApiModelProperty("工号中缀")
    private String employeeNumberInfix;

    /**
     * 流水号位数
     */
    @ApiModelProperty("流水号位数")
    private Short digitCapacity;

    /**
     * 工号后缀
     */
    @ApiModelProperty("工号后缀")
    private String employeeNumberSuffix;

    private static final long serialVersionUID = 1L;

















}
