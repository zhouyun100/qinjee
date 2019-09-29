/**
 * 文件名：RoleGroupVO
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
@ApiModel(description = "角色树")
@Data
@NoArgsConstructor
public class RoleGroupVO {
    /**
     * 角色组ID
     */
    @ApiModelProperty("角色组ID")
    private Integer roleGroupId;

    /**
     * 角色组名称
     */
    @ApiModelProperty("角色组名称")
    private String roleGroupName;

    /**
     * 角色类型
     */
    @ApiModelProperty("角色类型")
    private String roleType;

    /**
     * 父角色组ID
     */
    @ApiModelProperty("父角色组ID")
    private Integer parentRoleGroupId;

    /**
     * 子集角色组列表
     */
    @ApiModelProperty("子集角色组列表")
    private List<RoleGroupVO> childRoleGroupList;

}
