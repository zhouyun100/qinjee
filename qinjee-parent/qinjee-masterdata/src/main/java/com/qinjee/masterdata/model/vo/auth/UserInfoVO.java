/**
 * 文件名：UserInfoVO
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/16
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.model.vo.auth;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.qinjee.utils.QueryColumn;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 周赟
 * @version 1.0.0
 * @date  2019年09月12日
 */
@ApiModel(description = "用户信息类")
@Data
@NoArgsConstructor
@JsonInclude
public class UserInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    @QueryColumn("user_id")
    private int userId;

    /**
     * 用户姓名
     */
    @ApiModelProperty("用户姓名")
    @QueryColumn("user_name")
    private String userName;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    @QueryColumn("phone")
    private String phone;

    /**
     * 电子邮件
     */
    @ApiModelProperty("电子邮件")
    @QueryColumn("email")
    private String email;

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    @QueryColumn("company_id")
    private Integer companyId;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    @QueryColumn("company_name")
    private String companyName;

    /**
     * 档案ID
     */
    @ApiModelProperty("档案ID")
    @QueryColumn("archive_id")
    private Integer archiveId;
}
