/**
 * 文件名：RoleDataLevelAuthVO
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

import java.util.List;

/**
 * @author 周赟
 * @version 1.0.0
 * @date  2019年09月12日
 */
@ApiModel(description = "角色数据级权限类")
@Data
@NoArgsConstructor
public class RoleDataLevelAuthVO {

    /**
     * 角色ID
     */
    @ApiModelProperty("角色ID")
    private Integer roleId;

    /**
     * 数据级权限
     */
    @ApiModelProperty("数据级权限")
    private List<DataLevelAuthVO> childDataLevelAuthList;

}
