/**
 * 文件名：SmsRecordService
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/16
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.sms;

import com.qinjee.masterdata.model.entity.SendMessageModel;

/**
 * @author 周赟
 * @date 2019/9/18
 */
public interface SmsRecordService {
    /**
     * 发送短信验证码
     */
    void sendMessage(SendMessageModel sendMessageModel) throws Exception;
    /**
     * 记录批量发送登录短信验证码
     * @param phone
     */
    void sendSmsLoginCode(String phone);

}
