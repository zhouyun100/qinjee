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

import com.qinjee.masterdata.dao.SmsConfigDao;
import com.qinjee.masterdata.model.entity.SmsConfig;
import com.qinjee.masterdata.service.sms.SmsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsConfigServiceImpl implements SmsConfigService {

    private static final String BUSINESS_TYPE_LOGIN_CODE = "LOGIN_CODE";

    @Autowired
    private SmsConfigDao smsConfigDao;

    @Override
    public SmsConfig selectLoginCodeSmsConfig() {

        SmsConfig smsConfig = null;
        List<SmsConfig> smsConfigList = smsConfigDao.selectByBusinessType(BUSINESS_TYPE_LOGIN_CODE);
        if(!smsConfigList.isEmpty() && smsConfigList.size() > 0){
            smsConfig = smsConfigList.get(0);
        }
        return smsConfig;
    }
}
