/**
 * 文件名：RequestArchivePageVO
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/26
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.model.vo.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qinjee.model.request.PageVo;
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
 * @date  2019年09月26日
 */
@ApiModel(description = "权限移交请求参数封装类")
@Data
@NoArgsConstructor
@JsonInclude
public class RequestAuthHandoverVO implements Serializable {

    /**
     * 档案ID
     */
    @ApiModelProperty(name = "archiveId", value = "档案ID")
    private Integer archiveId;

    /**
     * 移交人档案ID
     */
    @ApiModelProperty(name = "handoverArchiveId", value = "移交人档案ID")
    private Integer handoverArchiveId;

    /**
     * 接收人档案ID
     */
    @ApiModelProperty(name = "acceptArchiveId", value = "接收人档案ID")
    private Integer acceptArchiveId;

    /**
     * 托管人档案ID
     */
    @ApiModelProperty(name = "trusteeshipArchiveId", value = "托管人档案ID")
    private Integer trusteeshipArchiveId;

    /**
     * 托管开始时间
     */
    @ApiModelProperty(name = "trusteeshipBeginTime", value = "托管开始时间")
    private String trusteeshipBeginTime;

    /**
     * 托管结束时间
     */
    @ApiModelProperty(name = "trusteeshipEndTime", value = "托管结束时间")
    private String trusteeshipEndTime;

    /**
     * 角色ID
     */
    @ApiModelProperty(name = "roleIdList", value = "角色ID")
    private List<Integer> roleIdList;

    /**
     * 机构ID
     */
    @ApiModelProperty(name = "orgIdList", value = "机构ID")
    private List<Integer> orgIdList;

}
