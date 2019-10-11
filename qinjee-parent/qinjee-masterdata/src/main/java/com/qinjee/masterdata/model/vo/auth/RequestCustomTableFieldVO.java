/**
 * 文件名：RequestCustomTableFieldVO
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
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录自定义表字段封装类
 * @author 周赟
 * @date 2019/9/24
 */
@Data
public class RequestCustomTableFieldVO {

    /**
     * 表ID
     */
    @ApiModelProperty("表ID")
    @QueryColumn("table_id")
    private Integer tableId;

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
     * 读写CODE
     */
    @ApiModelProperty("读写CODE")
    @QueryColumn("read_write_code")
    private String readWriteCode;

    /**
     * 操作人ID
     */
    @ApiModelProperty("操作人ID")
    @QueryColumn("operator_id")
    private Integer operatorId;

    /**
     * 是否删除
     */
    @ApiModelProperty("是否删除")
    @QueryColumn("is_delete")
    private Integer isDelete;
}
