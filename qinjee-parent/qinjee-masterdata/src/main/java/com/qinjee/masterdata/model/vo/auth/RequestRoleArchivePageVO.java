/**
 * 文件名：RequestRoleArchivePageVO
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/27
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

/**
 * @author 周赟
 * @version 1.0.0
 * @date  2019年09月27日
 */
@ApiModel(description = "角色人员分页请求参数封装类")
@Data
@NoArgsConstructor
@JsonInclude
public class RequestRoleArchivePageVO extends PageVo implements Serializable {
    /**
     * 角色ID
     */
    @ApiModelProperty("角色ID")
    private Integer roleId;
}
