/**
 * 文件名：RequestRoleArchiveVO
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
 * @date 2019/11/20
 */
@ApiModel(description = "角色新增或移除用户请求参数封装类")
@Data
@NoArgsConstructor
@JsonInclude
public class RequestRoleArchiveVO implements Serializable {

    /**
     * 角色ID
     */
    @ApiModelProperty(name = "roleId", value = "角色ID")
    private Integer roleId;

    /**
     * 档案ID集合
     */
    @ApiModelProperty(name = "archiveIdList", value = "档案ID集合")
    private List<Integer> archiveIdList;
}
