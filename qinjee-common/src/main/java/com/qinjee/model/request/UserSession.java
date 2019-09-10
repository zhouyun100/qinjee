/**
 * 文件名：UserSession
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/10
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.model.request;

import lombok.Data;

@Data
public class UserSession {

    /**
     * 用户ID
     */
    private int userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 企业ID
     */
    private Integer companyId;

    /**
     * 档案ID
     */
    private Integer archiveId;



}
