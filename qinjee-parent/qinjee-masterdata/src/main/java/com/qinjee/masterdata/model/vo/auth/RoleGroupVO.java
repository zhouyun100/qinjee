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

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author 周赟
 * @version 1.0.0
 * @date  2019年09月12日
 */
@ApiModel(description = "角色树")
@Data
@NoArgsConstructor
@JsonInclude
public class RoleGroupVO implements Serializable {
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
     * 是否托管
     */
    @ApiModelProperty("是否托管")
    private Integer isTrusteeship;

    /**
     * 托管开始时间
     */
    @ApiModelProperty("托管开始时间")
    private Date trusteeshipBeginTime;

    /**
     * 托管结束时间
     */
    @ApiModelProperty("托管结束时间")
    private Date trusteeshipEndTime;

    /**
     * 托管人
     */
    @ApiModelProperty("托管人")
    private String trusteeshipUserName;

    /**
     * 父角色组ID
     */
    @ApiModelProperty("父角色组ID")
    private Integer parentRoleGroupId;

    /**
     * 是否拥有该角色
     */
    @ApiModelProperty("是否拥有该角色(1:是,0:否)")
    private Integer hasRole;

    /**
     * 是否系统内置角色
     */
    @ApiModelProperty("是否系统内置角色(1:是,0:否)")
    private Integer isSystemDefine;

    /**
     * 子集角色组列表
     */
    @ApiModelProperty("子集角色组列表")
    private List<RoleGroupVO> childRoleGroupList;

}
