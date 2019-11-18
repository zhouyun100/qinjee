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

import com.qinjee.model.request.PageVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 周赟
 * @version 1.0.0
 * @date  2019年09月26日
 */
@ApiModel(description = "人员分页请求参数封装类")
@Data
@NoArgsConstructor
public class RequestArchivePageVO extends PageVo {

    /**
     * 用户姓名或者工号
     */
    @ApiModelProperty(name = "userName", value = "用户姓名或者工号", required = true)
    private String userName;

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Integer companyId;
}
