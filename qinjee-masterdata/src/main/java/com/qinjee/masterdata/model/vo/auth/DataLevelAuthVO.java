/**
 * 文件名：DataLevelAuthVO
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/12
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.model.vo.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 周赟
 * @version 1.0.0
 * @date  2019年09月12日
 */
@ApiModel(description = "数据级权限类")
@Data
@NoArgsConstructor
public class DataLevelAuthVO {

    /**
     * 是否有左括号
     */
    @ApiModelProperty("是否有左括号")
    private Integer isLeftBrackets;

    /**
     * 字段ID
     */
    @ApiModelProperty("字段ID")
    private Integer fieldId;

    /**
     * 操作符
     */
    @ApiModelProperty("操作符")
    private String operateSymbol;

    /**
     * 字段值
     */
    @ApiModelProperty("字段值")
    private String fieldValue;

    /**
     * 是否有右括号
     */
    @ApiModelProperty("是否有右括号")
    private Integer isRightBrackets;

    /**
     * 连接符
     */
    @ApiModelProperty("连接符")
    private String linkSymbol;

}
