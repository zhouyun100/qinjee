/**
 * 文件名：CustomArchiveTableFieldVO
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/24
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.model.vo.auth;

import com.qinjee.utils.QueryColumn;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 周赟
 * @version 1.0.0
 * @date  2019年09月24日
 */
@ApiModel(description = "自定义人员表字段类")
@Data
@NoArgsConstructor
public class CustomArchiveTableFieldVO {

    /**
     * 表ID
     */
    @ApiModelProperty("表ID")
    private Integer tableId;

    /**
     * 物理表名
     */
    @ApiModelProperty("物理表名")
    private String tableCode;

    /**
     * 功能CODE
     */
    @ApiModelProperty("功能CODE")
    private String funcCode;

    /**
     * 表名
     */
    @ApiModelProperty("表名")
    private String tableName;

    /**
     * 字段ID
     */
    @ApiModelProperty("字段ID")
    private Integer fieldId;

    /**
     * 物理字段名
     */
    @ApiModelProperty("物理字段名")
    private String fieldCode;

    /**
     * 字段名
     */
    @ApiModelProperty("字段名")
    private String fieldName;

    /**
     * 读写CODE（WRITE：读写，READ：只读）
     */
    @ApiModelProperty("是否有字段权限")
    private String readWriteCode;
}
