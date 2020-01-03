/**
 * 文件名：UserLoginService
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/16
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.userinfo;

import com.qinjee.masterdata.model.entity.UserInfo;
import com.qinjee.masterdata.model.vo.auth.MenuVO;
import com.qinjee.masterdata.model.vo.auth.UserInfoVO;

import java.util.List;

/**
 * 用户登录信息相关service
 *
 * @author 周赟
 * @date 2019.9.16
 */
public interface UserLoginService {
    /**
     * 根据账号密码查询用户信息
     * @param account 用户名/手机号/邮箱
     * @param password 密码
     * @return
     */
    List<UserInfoVO> searchUserInfoByAccountAndPassword(String account, String password);

    /**
     * 根据手机号查询用户信息
     * @param phone
     * @return
     */
    List<UserInfoVO> searchUserInfoByPhone(String phone);

    /**
     * 根据用户ID和企业ID查询用户信息
     * @param userId
     * @param companyId
     * @return
     */
    UserInfoVO searchUserInfoByUserIdAndCompanyId(Integer userId, Integer companyId);

    /**
     * 根据用户ID修改用户密码
     *
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    int updateUserPasswordByUserIdAndPassword(Integer userId, String oldPassword, String newPassword);

    /**
     * 根据档案ID和企业ID查询功能菜单树
     * @param archiveId
     * @param companyId
     * @return
     */
    List<MenuVO> searchUserMenuTreeByArchiveIdAndCompanyId(Integer archiveId, Integer companyId);

    /**
     * 根据手机号和企业ID查询用户登录账号主键userId
     * @param phone
     * @param companyId
     * @return
     */
    int getUserIdByPhone(String phone,Integer companyId);

    /**
     * 根据微信code调用查询用户信息
     * @param code
     * @return
     */
    UserInfoVO searchUserInfoByWeChatCode(String code);

    /**
     * 根据手机号查询登录用户详情
     * @param phone
     * @return
     */
    UserInfo searchUserInfoDetailByPhone(String phone);
}
