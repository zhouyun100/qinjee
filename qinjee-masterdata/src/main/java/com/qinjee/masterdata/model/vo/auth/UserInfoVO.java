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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 周赟
 * @version 1.0.0
 * @date  2019年09月12日
 */
@ApiModel(description = "用户信息类")
@Data
@NoArgsConstructor
public class UserInfoVO {

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private int userId;

    /**
     * 用户姓名
     */
    @ApiModelProperty("用户姓名")
    private String userName;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 电子邮件
     */
    @ApiModelProperty("电子邮件")
    private String email;

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Integer companyId;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String companyName;

    /**
     * 档案ID
     */
    @ApiModelProperty("档案ID")
    private Integer archiveId;
}
