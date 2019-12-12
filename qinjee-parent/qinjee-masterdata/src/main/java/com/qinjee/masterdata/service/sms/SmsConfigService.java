/**
 * 文件名：SmsConfigService
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/16
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.sms;

import com.qinjee.masterdata.model.entity.SmsConfig;

/**
 * @author 周赟
 * @date 2019/9/18
 */
public interface SmsConfigService {

    /**
     * 查询手机号验证码登录短信配置信息
     * @return
     */
    SmsConfig selectLoginCodeSmsConfig();


    /**
     * 查询预入职短信配置信息
     * @return
     */
    SmsConfig selectEntryRegistrationSmsConfig();

    /**
     * 查询预入职手机号验证码登录短信配置信息
     * @return
     */
    SmsConfig selectPreLoginCodeSmsConfig();
}
