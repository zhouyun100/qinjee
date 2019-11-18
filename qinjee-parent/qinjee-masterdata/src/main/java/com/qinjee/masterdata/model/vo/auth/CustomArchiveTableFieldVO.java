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
    @QueryColumn("table_id")
    private Integer tableId;

    /**
     * 物理表名
     */
    @ApiModelProperty("物理表名")
    @QueryColumn("table_code")
    private String tableCode;

    /**
     * 表名
     */
    @ApiModelProperty("表名")
    private String tableName;

    /**
     * 功能CODE
     */
    @ApiModelProperty("功能CODE")
    @QueryColumn("func_code")
    private String funcCode;

    /**
     * 档案ID
     */
    @ApiModelProperty("档案ID")
    @QueryColumn("archive_id")
    private Integer archiveId;

    /**
     * 角色ID
     */
    @ApiModelProperty("角色ID")
    @QueryColumn("role_id")
    private Integer roleId;

    /**
     * 字段ID
     */
    @ApiModelProperty("字段ID")
    @QueryColumn("field_id")
    private Integer fieldId;

    /**
     * 物理字段名
     */
    @ApiModelProperty("物理字段名")
    @QueryColumn("field_code")
    private String fieldCode;

    /**
     * 字段名
     */
    @ApiModelProperty("字段名")
    private String fieldName;

    /**
     * 操作人ID
     */
    @ApiModelProperty("操作人ID")
    @QueryColumn("operator_id")
    private Integer operatorId;

    /**
     * 读写CODE(READ<ADDWRITE<WRITE)
     */
    @ApiModelProperty("读写CODE")
    @QueryColumn("read_write_code")
    private String readWriteCode;

    /**
     * 是否删除
     */
    @ApiModelProperty("是否删除")
    @QueryColumn("is_delete")
    private Integer isDelete;

}
