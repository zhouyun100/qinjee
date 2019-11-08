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

import com.qinjee.masterdata.dao.auth.UserLoginDao;
import com.qinjee.masterdata.model.vo.auth.MenuVO;
import com.qinjee.masterdata.model.vo.auth.RequestLoginVO;
import com.qinjee.masterdata.model.vo.auth.UserInfoVO;
import com.qinjee.masterdata.service.auth.RoleAuthService;
import com.qinjee.masterdata.service.auth.UserLoginService;
import com.qinjee.utils.RegexpUtils;
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
         * 过滤菜单功能节点，只提取枝节点和叶节点
         */
        List<MenuVO> allMenuVOList = menuVOList.stream().filter(menu -> {
            if(menu.getFuncType().equals("CATEGORY") || menu.getFuncType().equals("NODE")){
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

}
