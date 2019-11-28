/**
 * 文件名：SmsConfigServiceImpl
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/16
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.sms.impl;

import com.qinjee.masterdata.dao.sms.SmsConfigDao;
import com.qinjee.masterdata.model.entity.SmsConfig;
import com.qinjee.masterdata.service.sms.SmsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 周赟
 * @date 2019/9/18
 */
@Service
public class SmsConfigServiceImpl implements SmsConfigService {

    private static final String BUSINESS_TYPE_LOGIN_CODE = "LOGIN_CODE";
    private static final String BUSINESS_TYPE_ENTRY_REGISTRATION = "ENTRY_REGISTRATION";

    @Autowired
    private SmsConfigDao smsConfigDao;

    @Override
    public SmsConfig selectLoginCodeSmsConfig() {

        SmsConfig smsConfig = smsConfigDao.selectByBusinessType(BUSINESS_TYPE_LOGIN_CODE);
        return smsConfig;
    }

    @Override
    public SmsConfig selectEntryRegistrationSmsConfig() {
        SmsConfig smsConfig = smsConfigDao.selectByBusinessType(BUSINESS_TYPE_ENTRY_REGISTRATION);
        return smsConfig;
    }
}
