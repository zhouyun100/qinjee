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
import com.qinjee.masterdata.model.vo.staff.FieldValueForSearch;
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
@ApiModel(description = "人员分页请求参数封装类")
@Data
@NoArgsConstructor
@JsonInclude
public class RequestArchivePageVO extends PageVo implements Serializable {
    /**
     * 表头查询参数
     */
    private List<FieldValueForSearch> tableHeadParamList;

    /**
     * 机构ID
     */
    @ApiModelProperty(name = "orgId", value = "机构ID(Example:35:勤杰广西分公司青秀区办事处1号办公室)")
    private Integer orgId;

    /**
     * 角色ID
     */
    @ApiModelProperty(name = "roleId", value = "角色ID")
    private Integer roleId;

    /**
     * 用户姓名或者工号
     */
    @ApiModelProperty(name = "userName", value = "用户姓名或者工号(非必填，为空时查全量，不为空时至少2位字符)")
    private String userName;

    /**
     * 企业ID
     */
    @ApiModelProperty(name = "companyId", value = "企业ID(前端不用提供，后端自动从session中取值)")
    private Integer companyId;

    /**
     * 当前时间
     */
    private Date currentDateTime;
}
