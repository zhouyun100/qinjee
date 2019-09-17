/**
 * 文件名：UserLoginDao
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/16
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.dao.auth;

import com.qinjee.masterdata.model.entity.UserInfo;
import com.qinjee.masterdata.model.vo.auth.RequestUserLoginVO;
import com.qinjee.masterdata.model.vo.auth.UserInfoVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserLoginDao {

    /**
     * 根据账号密码查询用户信息
     * @param userLoginVO 用户登录参数信息
     * @return
     */
    List<UserInfoVO> searchUserInfoByAccountAndPassword(RequestUserLoginVO userLoginVO);

    /**
     * 根据手机号查询用户信息
     * @param phone
     * @return
     */
    List<UserInfoVO> searchUserInfoByPhone(String phone);

    /**
     * 根据用户ID和企业ID查询用户信息
     * @param userLoginVO
     * @return
     */
    UserInfoVO searchUserInfoByUserIdAndCompanyId(RequestUserLoginVO userLoginVO);

    /**
     * 根据用户ID修改用户信息
     * @param userLoginVO
     * @return
     */
    int updateUserPasswordByUserIdAndPassword(RequestUserLoginVO userLoginVO);

}