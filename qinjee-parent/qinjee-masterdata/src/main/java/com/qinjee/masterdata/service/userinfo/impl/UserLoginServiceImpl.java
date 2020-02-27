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
package com.qinjee.masterdata.service.userinfo.impl;

import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.userinfo.UserLoginDao;
import com.qinjee.masterdata.model.entity.UserInfo;
import com.qinjee.masterdata.model.vo.auth.MenuVO;
import com.qinjee.masterdata.model.vo.auth.RequestLoginVO;
import com.qinjee.masterdata.model.vo.auth.UserInfoVO;
import com.qinjee.masterdata.service.auth.RoleAuthService;
import com.qinjee.masterdata.service.userinfo.UserLoginService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.utils.MD5Utils;
import com.qinjee.utils.RegexpUtils;
import com.qinjee.utils.WeChatUtils;
import entity.WeChatToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 *
 * @author 周赟
 * @date 2019/9/18
 */
@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private UserLoginDao userLoginDao;

    @Autowired
    private RoleAuthService roleAuthService;

    @Override
    public List<UserInfoVO> searchUserInfoByAccountAndPassword(String account, String password) {
        if(account.isEmpty() || password.isEmpty()){
            return null;
        }
        RequestLoginVO userLoginVO = new RequestLoginVO();
        userLoginVO.setAccount(account);
        userLoginVO.setPassword(MD5Utils.getMd5(password));
        return userLoginDao.searchUserInfoByAccountAndPassword(userLoginVO);
    }

    @Override
    public List<UserInfo> searchUserInfoByAccount(String account) {
        if(StringUtils.isNoneBlank(account)){
            return userLoginDao.searchUserInfoByAccount(account);
        }
        return null;
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
        RequestLoginVO userLoginVO = new RequestLoginVO();
        userLoginVO.setUserId(userId);
        userLoginVO.setCompanyId(companyId);
        return userLoginDao.searchUserInfoByUserIdAndCompanyId(userLoginVO);
    }

    @Override
    public int updateUserPasswordByUserIdAndPassword(Integer userId, String oldPassword, String newPassword) {
        RequestLoginVO userLoginVO = new RequestLoginVO();
        userLoginVO.setUserId(userId);
        userLoginVO.setPassword(oldPassword);
        userLoginVO.setNewPassword(newPassword);
        return userLoginDao.updateUserPasswordByUserIdAndPassword(userLoginVO);
    }

    @Override
    public List<MenuVO> searchUserMenuTreeByArchiveIdAndCompanyId(Integer archiveId, Integer companyId) {

        RequestLoginVO userLoginVO = new RequestLoginVO();
        userLoginVO.setArchiveId(archiveId);
        userLoginVO.setCompanyId(companyId);
        userLoginVO.setCurrentDateTime(new Date());
        List<MenuVO> menuVOList = userLoginDao.searchUserMenuListByArchiveIdAndCompanyId(userLoginVO);

        /**
         * 如果菜单列表为空则直接返回null
         */
        if(CollectionUtils.isEmpty(menuVOList)){
            return null;
        }

        /**
         * 过滤菜单功能节点，只加载菜单目录和菜单
         */
        List<MenuVO> allMenuVOList = menuVOList.stream().filter(menu -> {
            if(StringUtils.isNoneBlank(menu.getFuncType()) && (menu.getFuncType().equals("CATEGORY") || menu.getFuncType().equals("NODE"))){
                return true;
            }else{
                return false;
            }
        }).collect(Collectors.toList());

        /**
         * 提取当前菜单树的一级节点
         */
        List<MenuVO> firstLevelMenuList = menuVOList.stream().filter(menu -> {
            if(menu.getParentMenuId() == 0){
                return true;
            }else{
                return false;
            }
        }).collect(Collectors.toList());

        /**
         * 处理所有菜单列表以树形结构展示
         */
        roleAuthService.handlerMenuToTree(allMenuVOList,firstLevelMenuList);

        return firstLevelMenuList;
    }

    @Override
    public int getUserIdByPhone(String phone, Integer companyId) {
        int userId = 0;
        UserInfo userInfo = userLoginDao.searchUserInfoDetailByPhone(phone);
        if(userInfo != null && userInfo.getUserId() != null){
            UserInfoVO userInfoVO = userLoginDao.searchUserCompanyByUserIdAndCompanyId(userInfo.getUserId(),companyId);
            if(userInfoVO == null){
                userLoginDao.addCompanyUserInfo(companyId,userInfo.getUserId());
            }
            userId = userInfo.getUserId();
        }else{

            if(RegexpUtils.checkPhone(phone)){
                userInfo = new UserInfo();
                userInfo.setPhone(phone);
                int phoneLength = phone.length();
                userInfo.setPassword(MD5Utils.getMd5(phone.substring(phoneLength-6,phoneLength)));
                int resultCount = userLoginDao.addUserInfo(userInfo);
                if(resultCount > 0){
                    userId = userInfo.getUserId();
                    userLoginDao.addCompanyUserInfo(companyId,userId);
                }
            }
        }
        return userId;
    }

    @Override
    public UserInfoVO searchUserInfoByWeChatCode(String code) {
        UserInfoVO userInfoVO = null;
        WeChatToken weChatToken = WeChatUtils.getWeChatToken(code);
        if(weChatToken != null){

            userInfoVO = userLoginDao.searchUserInfoByOpenId(weChatToken.getOpenid());

            if(userInfoVO == null){
                ExceptionCast.cast(CommonCode.WECHAT_NO_BIND);
            }
        }else{
            ExceptionCast.cast(CommonCode.WECHAT_ACCESS_TOKEN);
        }
        return userInfoVO;
    }

    @Override
    public UserInfo searchUserInfoDetailByPhone(String phone) {
        return userLoginDao.searchUserInfoDetailByPhone(phone);
    }
}
