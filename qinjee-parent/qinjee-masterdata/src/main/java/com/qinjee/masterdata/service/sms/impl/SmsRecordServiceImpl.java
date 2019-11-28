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
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentDao;
import com.qinjee.masterdata.model.entity.SendMessageModel;
import com.qinjee.masterdata.model.entity.SmsConfig;
import com.qinjee.masterdata.model.entity.SmsRecord;
import com.qinjee.masterdata.redis.RedisClusterService;
import com.qinjee.masterdata.service.sms.SmsConfigService;
import com.qinjee.masterdata.service.sms.SmsRecordService;
import com.qinjee.utils.KeyUtils;
import com.qinjee.utils.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private static final String APPKEY = "91c94cbe664487bbfb072e717957e08f";
    private static final Integer APPID = 1400249114;

    @Autowired
    private SmsRecordDao smsRecordDao;

    @Autowired
    private RedisClusterService redisClusterService;

    @Autowired
    private SmsConfigService smsConfigService;
    @Autowired
    private  PreEmploymentDao preEmploymentDao;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendMessage(SendMessageModel sendMessageModel) throws Exception {
        Integer max = preEmploymentDao.selectMaxId();
        for (Integer integer : sendMessageModel.getList ()) {
            if (max < integer) {
                throw new Exception("id出错");
            }
        }
        List<String> phoneNumber = preEmploymentDao.getPhoneNumber(sendMessageModel.getList ());
        SendMessage.sendMessageMany(APPID, APPKEY, sendMessageModel.getTemplateId (), "勤杰软件", phoneNumber, sendMessageModel.getParams ());
    }

    @Override
    public void sendSmsLoginCode(String phone) {

        /**
         * 查询登录短信验证码的配置信息
         */
        SmsConfig smsConfig = smsConfigService.selectLoginCodeSmsConfig();

        /**
         * 生成6位随机数字验证码
         */
        String smsCode = KeyUtils.getNonceCodeNumber(6);
        List<String> params = new ArrayList<>();
        params.add(smsCode);
        params.add(String.valueOf(SMS_CODE_VALID_MINUTE));
        redisClusterService.setex("LOGIN_" + phone,SMS_CODE_VALID_MINUTE * 60, smsCode);

        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(phone);
        SendMessage.sendMessageMany(smsConfig.getAppId(),smsConfig.getAppKey(),smsConfig.getTemplateId(),smsConfig.getSmsSign(),phoneNumbers,params);

        //添加短信记录
        insertSmsRecord(smsConfig,phone,params);
    }

    private void insertSmsRecord(SmsConfig smsConfig,String phone,List<String> params){
        SmsRecord smsRecord = new SmsRecord();

        smsRecord.setPhone(phone);
        /**
         * 根据短信模板内容替换相应的参数内容
         */
        MessageFormat messageFormat = new MessageFormat(smsConfig.getTemplateMsg());
        String[] param = new String[params.size()];
        params.toArray(param);
        smsRecord.setSendMsg(messageFormat.format(param));
        smsRecord.setSendTime(new Date());
        smsRecord.setNationCode("86");
        smsRecord.setSmsConfigId(smsConfig.getSmsConfigId());
        smsRecordDao.insertSelective(smsRecord);
    }
}
