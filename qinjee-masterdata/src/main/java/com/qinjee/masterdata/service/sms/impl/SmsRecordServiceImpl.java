/**
 * 文件名：SmsRecordServiceImpl
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/18
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.sms.impl;

import com.qinjee.masterdata.dao.sms.SmsRecordDao;
import com.qinjee.masterdata.model.entity.SmsConfig;
import com.qinjee.masterdata.model.entity.SmsRecord;
import com.qinjee.masterdata.redis.RedisClusterService;
import com.qinjee.masterdata.service.sms.SmsConfigService;
import com.qinjee.masterdata.service.sms.SmsRecordService;
import com.qinjee.utils.KeyUtils;
import com.qinjee.utils.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

/**
 * @author 周赟
 * @date 2019/9/18
 */
@Service
public class SmsRecordServiceImpl implements SmsRecordService {

    /**
     * 短信验证码有效分钟数
     */
    private static final int SMS_CODE_VALID_MINUTE = 5;

    @Autowired
    private SmsRecordDao smsRecordDao;

//    @Autowired
    private RedisClusterService redisClusterService;

    @Autowired
    private SmsConfigService smsConfigService;

    @Override
    public void sendSmsLoginCode(String phone) {

        /**
         * 查询登录短信验证码的配置信息
         */
        SmsConfig smsConfig = smsConfigService.selectLoginCodeSmsConfig();
        String templateMsg = smsConfig.getTemplateMsg();

        /**
         * 生成6位随机数字验证码
         */
        String smsCode = KeyUtils.getNonceCodeNumber(6);
        String[] params = {smsCode,String.valueOf(SMS_CODE_VALID_MINUTE)};
        redisClusterService.setex("LOGIN_" + phone,SMS_CODE_VALID_MINUTE * 60, smsCode);

        String[] phoneNumbers = {phone};
        SendMessage.sendMessageMany(smsConfig.getAppId(),smsConfig.getAppKey(),smsConfig.getTemplateId(),smsConfig.getSmsSign(),phoneNumbers,params);

        SmsRecord smsRecord = new SmsRecord();

        smsRecord.setPhone(phone);
        /**
         * 根据短信模板内容替换相应的参数内容
         */
        templateMsg.replace("{1}",params[0]).replace("{2}",params[1]);
        smsRecord.setSendMsg(templateMsg);
        smsRecord.setSendTime(new Date());
        smsRecord.setNationCode("86");
        smsRecord.setSmsConfigId(smsConfig.getSmsConfigId());
        smsRecordDao.insertSelective(smsRecord);

    }
}
