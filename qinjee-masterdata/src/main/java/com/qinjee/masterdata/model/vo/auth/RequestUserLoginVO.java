/**
 * 文件名：RequestUserLoginVO
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/16
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.model.vo.auth;

import lombok.Data;

@Data
public class RequestUserLoginVO {

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 账号：用户名/手机号/邮箱
     */
    private String account;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 验证码
     */
    private String code;

    /**
     * 密码
     */
    private String password;

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 企业ID
     */
    private Integer companyId;
}
