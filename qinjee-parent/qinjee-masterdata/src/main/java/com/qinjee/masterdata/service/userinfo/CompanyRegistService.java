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

import com.qinjee.masterdata.model.vo.auth.UserInfoVO;
import com.qinjee.masterdata.model.vo.userinfo.CompanyRegistParamVO;

import java.util.Date;

/**
 * @author 周赟
 * @date 2020-01-02
 */
public interface CompanyRegistService {

    /**
     * 根据手机号查询注册的企业数量
     * @param phone
     * @return
     */
    Integer searchRegistCompanyCountByPhone(String phone);

    /**
     * 注册企业
     * @param companyRegistParamVO
     */
    UserInfoVO registCompany(CompanyRegistParamVO companyRegistParamVO);
}
