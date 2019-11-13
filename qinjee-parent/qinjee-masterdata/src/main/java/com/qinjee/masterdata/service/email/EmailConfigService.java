/**
 * 文件名：EmailConfigService
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/10/23
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.email;

import com.qinjee.masterdata.model.entity.EmailConfig;

/**
 * @author 周赟
 * @date 2019/10/23
 */
public interface EmailConfigService {

    /**
     * 根据企业ID查询邮箱配置信息
     * @param companyId
     * @return
     */
    EmailConfig getEmailConfigByCompanyId(Integer companyId);


}
