/**
 * 文件名：EmailConfigServiceImpl
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/10/23
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.email.impl;

import com.qinjee.masterdata.dao.email.EmailConfigDao;
import com.qinjee.masterdata.model.entity.EmailConfig;
import com.qinjee.masterdata.service.email.EmailConfigService;
import com.qinjee.utils.AesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 周赟
 * @date 2019/10/23
 */
@Service
public class EmailConfigServiceImpl implements EmailConfigService {

    /**
     * 勤杰企业ID
     */
    private static final Integer COMPANY_QINJEE_1 = 1;

    @Autowired
    private EmailConfigDao emailConfigDao;

    @Override
    public EmailConfig getEmailConfigByCompanyId(Integer companyId) {
        EmailConfig emailConfig = null;
        if(companyId != null){
            try {
                emailConfig = emailConfigDao.getEmailConfigByCompanyId(companyId);

                if(emailConfig == null){
                    // 获取平台默认的邮件配置信息(当企业未配置邮件信息时使用平台默认配置)
                    emailConfig = emailConfigDao.getEmailConfigByCompanyId(COMPANY_QINJEE_1);
                }

                // 邮件账号密码密文解密
                emailConfig.setPassword(AesUtils.aesDecode(emailConfig.getPassword(),emailConfig.getAccount()));

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return emailConfig;
    }
}
