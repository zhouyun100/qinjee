/**
 * 文件名：UserLoginServiceImpl
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/16
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.auth.impl;

import com.alibaba.druid.util.StringUtils;
import com.qinjee.masterdata.dao.auth.UserLoginDao;
import com.qinjee.masterdata.model.entity.UserInfo;
import com.qinjee.masterdata.model.vo.auth.RequestUserLoginVO;
import com.qinjee.masterdata.model.vo.auth.UserInfoVO;
import com.qinjee.masterdata.service.auth.UserLoginService;
import com.qinjee.utils.RegexpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private UserLoginDao userLoginDao;

    @Override
    public List<UserInfoVO> searchUserInfoByAccountAndPassword(String account, String password) {
        if(account.isEmpty() || password.isEmpty()){
            return null;
        }
        RequestUserLoginVO userLoginVO = new RequestUserLoginVO();
        userLoginVO.setAccount(account);
        userLoginVO.setPassword(password);
        return userLoginDao.searchUserInfoByAccountAndPassword(userLoginVO);
    }

    @Override
    public List<UserInfoVO> searchUserInfoByPhone(String phone) {
        if(phone.isEmpty() || !RegexpUtils.checkPhone(phone)){
            return null;
        }
        return userLoginDao.searchUserInfoByPhone(phone);
    }

    @Override
    public UserInfoVO searchUserInfoByUserIdAndCompanyId(Integer userId, Integer companyId) {
        if(null == userId || null == companyId){
            return null;
        }
        RequestUserLoginVO userLoginVO = new RequestUserLoginVO();
        userLoginVO.setUserId(userId);
        userLoginVO.setCompanyId(companyId);
        return userLoginDao.searchUserInfoByUserIdAndCompanyId(userLoginVO);
    }

    @Override
    public int updateUserPasswordByUserIdAndPassword(Integer userId, String oldPassword, String newPassword) {
        RequestUserLoginVO userLoginVO = new RequestUserLoginVO();
        userLoginVO.setUserId(userId);
        userLoginVO.setPassword(oldPassword);
        userLoginVO.setNewPassword(newPassword);
        return userLoginDao.updateUserPasswordByUserIdAndPassword(userLoginVO);
    }
}
