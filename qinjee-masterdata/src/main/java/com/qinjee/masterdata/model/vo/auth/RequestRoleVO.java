/**
 * 文件名：RequestRoleVO
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/20
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.model.vo.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 用户权限请求参数封装类
 * @author 周赟
 * @date 2019/9/20
 */
@Data
public class RequestRoleVO {

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    private Integer id;

    /**
     * 档案ID
     */
    @ApiModelProperty("档案ID")
    private Integer archiveId;

    /**
     * 角色ID
     */
    @ApiModelProperty("角色ID")
    private Integer roleId;

    /**
     * 机构ID
     */
    @ApiModelProperty("机构ID")
    private Integer orgId;

    /**
     * 移交人
     */
    @ApiModelProperty("移交人")
    private Integer handoverArchiveId;

    /**
     * 接收人
     */
    @ApiModelProperty("接收人")
    private Integer acceptArchiveId;

    /**
     * 托管开始时间
     */
    @ApiModelProperty("托管开始时间")
    private Date trusteeshipBeginTime;

    /**
     * 托管开始时间
     */
    @ApiModelProperty("托管开始时间")
    private Date trusteeshipEndTime;

    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    private Integer operatorId;

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Integer companyId;

}
