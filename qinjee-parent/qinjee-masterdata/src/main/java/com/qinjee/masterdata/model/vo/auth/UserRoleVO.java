/**
 * 文件名：UserRoleVO
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/20
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

/**
 * @author 周赟
 * @version 1.0.0
 * @date  2019年09月20日
 */
@ApiModel(description = "用户角色类")
@Data
@NoArgsConstructor
@JsonInclude
public class UserRoleVO implements Serializable {

    /**
     * 用户角色主键ID
     */
    @ApiModelProperty("用户角色主键ID")
    private Integer id;

    /**
     * 角色ID
     */
    @ApiModelProperty("角色ID")
    private Integer roleId;

    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    private String roleName;

    /**
     * 角色组ID
     */
    @ApiModelProperty("角色组ID")
    private Integer roleGroupId;

    /**
     * 是否系统定义
     */
    @ApiModelProperty("是否系统定义")
    private Short isSystemDefine;

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
}
