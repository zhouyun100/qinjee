package com.qinjee.masterdata.service.userinfo;

import com.qinjee.masterdata.model.entity.CompanyInfo;

import java.util.List;

/**
 * 用户信息相关service
 *
 * @author 周赟
 * @date 2020.3.6
 */
public interface UserInfoService {

    /**
     * 根据用户ID查询企业列表
     * @param userId
     * @return
     */
    List<CompanyInfo> selectCompanyListByUserId(Integer userId);

    /**
     * 根据userId和companyId切换登录企业
     * @param userId
     * @param companyId
     * @return
     */
    int changeCompany(Integer userId,Integer companyId);
}
