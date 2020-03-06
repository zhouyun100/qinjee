package com.qinjee.masterdata.service.userinfo.impl;

import com.qinjee.masterdata.dao.userinfo.UserInfoDao;
import com.qinjee.masterdata.model.entity.CompanyInfo;
import com.qinjee.masterdata.service.userinfo.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @author 周赟
 * @date 2020/3/6
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public List<CompanyInfo> selectCompanyListByUserId(Integer userId) {
        return userInfoDao.selectCompanyListByUserId(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int changeCompany(Integer userId, Integer companyId) {
        int resultCount = userInfoDao.setUserCompanyDefaultLogin(userId,companyId);
        resultCount += userInfoDao.setUserCompanyNoneDefaultLogin(userId,companyId);
        return resultCount;
    }
}
