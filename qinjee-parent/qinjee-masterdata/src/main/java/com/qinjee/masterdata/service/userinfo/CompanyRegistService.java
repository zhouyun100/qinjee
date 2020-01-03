/**
 * 文件名：CompanyRegistService
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2020/1/2
 * <p>
 * Copyright(C) 2020, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.userinfo;

import java.util.Date;

/**
 * @author 周赟
 * @date 2020-01-02
 */
public interface CompanyRegistService {

    /**
     * 注册企业
     * @param companyName
     * @param userNumber
     * @param validEndDate
     * @param phone
     * @param userName
     */
    void registCompany(String companyName, Integer userNumber, Date validEndDate, String phone, String userName);
}
