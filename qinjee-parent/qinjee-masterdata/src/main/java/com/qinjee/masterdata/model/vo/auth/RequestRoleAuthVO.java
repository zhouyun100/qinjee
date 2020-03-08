/**
 * 文件名：RequestRoleAuthVO
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/11/20
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
import java.util.List;

/**
 * @author 周赟
 * @date 2019/11/19
 */
@ApiModel(description = "角色授权请求参数封装类")
@Data
@NoArgsConstructor
@JsonInclude
public class RequestRoleAuthVO implements Serializable {

    /**
     * 档案ID
     */
    @ApiModelProperty(name = "archiveId", value = "档案ID")
    private Integer archiveId;

    /**
     * 角色ID
     */
    @ApiModelProperty(name = "roleId", value = "角色ID")
    private Integer roleId;

    /**
     * 功能菜单ID
     */
    @ApiModelProperty(name = "menuIdList", value = "功能菜单ID")
    private List<Integer> menuIdList ;

    /**
     * 机构ID
     */
    @ApiModelProperty(name = "orgIdList", value = "机构ID")
    private List<Integer> orgIdList;

    /**
     * 角色ID
     */
    @ApiModelProperty(name = "roleIdList", value = "角色ID")
    private List<Integer> roleIdList;
}
