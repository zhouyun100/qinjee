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
package com.qinjee.masterdata.dao.userinfo;

import com.qinjee.masterdata.model.entity.UserInfo;
import com.qinjee.masterdata.model.vo.auth.MenuVO;
import com.qinjee.masterdata.model.vo.auth.RequestLoginVO;
import com.qinjee.masterdata.model.vo.auth.UserInfoVO;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author
 * @date 2019/9/18
 */
@Repository
public interface UserLoginDao {

    /**
     * 根据账号密码查询用户信息
     * @param userLoginVO 用户登录参数信息
     * @return
     */
    List<UserInfoVO> searchUserInfoByAccountAndPassword(RequestLoginVO userLoginVO);

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
    UserInfoVO searchUserInfoByUserIdAndCompanyId(RequestLoginVO userLoginVO);

    /**
     * 根据用户ID修改用户信息
     * @param userLoginVO
     * @return
     */
    int updateUserPasswordByUserIdAndPassword(RequestLoginVO userLoginVO);

    /**
     * 根据档案ID和企业ID查询功能菜单列表
     * @param userLoginVO
     * @return
     */
    List<MenuVO> searchUserMenuListByArchiveIdAndCompanyId(RequestLoginVO userLoginVO);

    /**
     * 根据手机号查询登录用户详情
     * @param phone
     * @return
     */
    UserInfo searchUserInfoDetailByPhone(String phone);

    /**
     * 新增登录用户
     * @param userInfo
     * @return 新增登录用户主键userId
     */
    int addUserInfo(UserInfo userInfo);

    /**
     * 根据userId和companyId查询用户企业关联记录
     * @param companyId
     * @param userId
     * @return
     */
    UserInfoVO searchUserCompanyByUserIdAndCompanyId(Integer companyId,Integer userId);

    /**
     * 新增登录用户与企业关系记录
     * @param companyId
     * @param userId
     * @return
     */
    int addCompanyUserInfo(Integer companyId,Integer userId);

    /**
     * 根据openid查询用户信息
     * @param openId
     * @return
     */
    UserInfoVO searchUserInfoByOpenId(String openId);
}
