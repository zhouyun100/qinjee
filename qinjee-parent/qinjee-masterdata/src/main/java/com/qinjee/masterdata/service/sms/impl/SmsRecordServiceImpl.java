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

import com.alibaba.fastjson.JSON;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.CompanyInfoDao;
import com.qinjee.masterdata.dao.sms.SmsRecordDao;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentDao;
import com.qinjee.masterdata.model.entity.SmsConfig;
import com.qinjee.masterdata.model.entity.SmsRecord;
import com.qinjee.masterdata.redis.RedisClusterService;
import com.qinjee.masterdata.service.sms.SmsConfigService;
import com.qinjee.masterdata.service.sms.SmsRecordService;
import com.qinjee.masterdata.service.sys.SysDictService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.utils.KeyUtils;
import com.qinjee.utils.SendMessage;
import com.qinjee.utils.ShortEncryptUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;

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

    @Autowired
    private RedisClusterService redisClusterService;

    @Autowired
    private SmsConfigService smsConfigService;
    @Autowired
    private  PreEmploymentDao preEmploymentDao;
    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private CompanyInfoDao companyInfoDao;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendMessageSms(List<Integer> list, Integer templateId, UserSession userSession) {
        Integer i=0;
        SmsConfig smsConfig = smsConfigService.selectEntryRegistrationSmsConfig ();
        //获取预入职的信息
        Map < Integer, Map < String, String > > integerMapMap = preEmploymentDao.selectNameAndOrg ( list );
        String baseShortUrl = sysDictService.searchSysDictByTypeAndCode ( "SHORT_URL", "EMPLOYMENT_REGISTER" ).getDictValue ();
        for (Map.Entry < Integer, Map < String, String > > integerMapEntry : integerMapMap.entrySet ()) {
            Map < String, String > value = integerMapEntry.getValue ();
            String userName = value.get ( "user_name" );
            String companyName=companyInfoDao.selectByPrimaryKey ( userSession.getCompanyId () ).getCompanyName ();
//            String companyId = String.valueOf (value.get ( "company_id" ));
            String phone = value.get ( "phone" );
            if(StringUtils.isEmpty(phone)){
                CommonCode msgCode=CommonCode.MSG_IS_MISTAKE;
                String message=userName+"没有手机号，无法发送";
                msgCode.setMessage(message);
                ExceptionCast.cast(msgCode);
            }
            List<String> phoneNumbers = new ArrayList<>();
            phoneNumbers.add(phone);
            //拼接短链接
            String keyValue = "preId=" + integerMapEntry.getKey () +"&templateId="+ templateId+"&companyId="+userSession.getCompanyId ();
            String key = ShortEncryptUtils.shortEncrypt ( keyValue );
            Map<String,Integer> stringMap = new HashMap <> (  );
            stringMap.put ( "preId",  integerMapEntry.getKey ());
            stringMap.put ( "templateId",  templateId);
            stringMap.put ( "companyId",  userSession.getCompanyId ());
            redisClusterService.setex(key,2*60*60,JSON.toJSONString ( stringMap));
            String url = baseShortUrl + key;
            List < String > params = new ArrayList <> ( 3 );
            //拼接参数
            params.add ( userName );
            params.add ( companyName );
            params.add ( url );
            //发送短信
            try {
                SendMessage.sendMessageMany ( smsConfig.getAppId (), smsConfig.getAppKey (), smsConfig.getTemplateId (), "勤杰软件", phoneNumbers,params  );
                i++;
            } catch (Exception e) {
                e.printStackTrace ();
            }
//            SendMessage.sendMailSingle ( smsConfig.getAppId (),smsConfig.getAppKey (),smsConfig.getTemplateId (),"勤杰软件", phoneNumbers,params);
            //添加短信记录
            insertSmsRecord(smsConfig,phone,params);
        }
        System.out.println (i);
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

    @Override
    public void sendSmsRegistCode(String phone) {
        /**
         * 查询注册短信验证码的配置信息
         */
        SmsConfig smsConfig = smsConfigService.selectRegistCodeSmsConfig();

        /**
         * 生成6位随机数字验证码
         */
        String smsCode = KeyUtils.getNonceCodeNumber(6);
        List<String> params = new ArrayList<>();
        params.add(smsCode);
        params.add(String.valueOf(SMS_CODE_VALID_MINUTE));
        redisClusterService.setex("REGIST_" + phone,SMS_CODE_VALID_MINUTE * 60, smsCode);

        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(phone);
        SendMessage.sendMessageMany(smsConfig.getAppId(),smsConfig.getAppKey(),smsConfig.getTemplateId(),smsConfig.getSmsSign(),phoneNumbers,params);

        //添加短信记录
        insertSmsRecord(smsConfig,phone,params);
    }

    @Override
    public void sendSmsWechatBindCode(String phone) {
        /**
         * 查询微信绑定短信验证码的配置信息
         */
        SmsConfig smsConfig = smsConfigService.selectWechatBindCodeSmsConfig();

        /**
         * 生成6位随机数字验证码
         */
        String smsCode = KeyUtils.getNonceCodeNumber(6);
        List<String> params = new ArrayList<>();
        params.add(smsCode);
        params.add(String.valueOf(SMS_CODE_VALID_MINUTE));
        redisClusterService.setex("WECHAT_BIND_" + phone,SMS_CODE_VALID_MINUTE * 60, smsCode);

        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(phone);
        SendMessage.sendMessageMany(smsConfig.getAppId(),smsConfig.getAppKey(),smsConfig.getTemplateId(),smsConfig.getSmsSign(),phoneNumbers,params);

        //添加短信记录
        insertSmsRecord(smsConfig,phone,params);
    }

    @Override
    public void sendSmsForgetPasswordCode(String phone) {
        /**
         * 查询忘记密码短信验证码的配置信息
         */
        SmsConfig smsConfig = smsConfigService.selectForgetPasswordCodeSmsConfig();

        /**
         * 生成6位随机数字验证码
         */
        String smsCode = KeyUtils.getNonceCodeNumber(6);
        List<String> params = new ArrayList<>();
        params.add(smsCode);
        params.add(String.valueOf(SMS_CODE_VALID_MINUTE));
        redisClusterService.setex("FORGET_PASSWORD_" + phone,SMS_CODE_VALID_MINUTE * 60, smsCode);

        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(phone);
        SendMessage.sendMessageMany(smsConfig.getAppId(),smsConfig.getAppKey(),smsConfig.getTemplateId(),smsConfig.getSmsSign(),phoneNumbers,params);

        //添加短信记录
        insertSmsRecord(smsConfig,phone,params);
    }

    @Override
    public void sendSmsPreLoginCode(String phone) {
        /**
         * 查询预入职手机号验证码登录短信配置信息
         */
        SmsConfig smsConfig = smsConfigService.selectPreLoginCodeSmsConfig();

        /**
         * 生成6位随机数字验证码
         */
        String smsCode = KeyUtils.getNonceCodeNumber(6);
        List<String> params = new ArrayList<>();
        params.add(smsCode);
        params.add(String.valueOf(SMS_CODE_VALID_MINUTE));
        redisClusterService.setex("PRE_LOGIN_" + phone,SMS_CODE_VALID_MINUTE * 60, smsCode);

        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(phone);
        SendMessage.sendMessageMany(smsConfig.getAppId(),smsConfig.getAppKey(),smsConfig.getTemplateId(),smsConfig.getSmsSign(),phoneNumbers,params);

        //添加短信记录
        insertSmsRecord(smsConfig,phone,params);
    }

    @Override
    public boolean checkPreLoginCodeByPhoneAndCode(String phone, String code) {
        if(StringUtils.isNoneBlank(phone) && StringUtils.isNoneBlank(code)){
            String redisPhoneLoginCode = redisClusterService.get("PRE_LOGIN_" + phone);
            if(StringUtils.isNoneBlank(redisPhoneLoginCode) && redisPhoneLoginCode.equals(code)){
                return true;
            }
        }
        return false;
    }

    private void insertSmsRecord(SmsConfig smsConfig, String phone, List<String> params){
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
