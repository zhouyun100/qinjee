/**
 * 文件名：AuthHandoverServiceImpl
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/20
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.auth.impl;

import com.qinjee.masterdata.dao.auth.AuthHandoverDao;
import com.qinjee.masterdata.model.vo.auth.MenuVO;
import com.qinjee.masterdata.model.vo.auth.OrganizationVO;
import com.qinjee.masterdata.model.vo.auth.RequestRoleVO;
import com.qinjee.masterdata.model.vo.auth.UserRoleVO;
import com.qinjee.masterdata.service.auth.AuthHandoverService;
import com.qinjee.masterdata.service.auth.RoleAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * 权限移交实现类
 * @author 周赟
 * @date 2019/9/20
 */
@Service
public class AuthHandoverServiceImpl implements AuthHandoverService {

    @Autowired
    private AuthHandoverDao authHandoverDao;

    @Autowired
    private RoleAuthService roleAuthService;

    @Override
    public List<MenuVO> searchRoleAuthTree(Integer operatorId, Integer roleId, Integer companyId) {
        List<MenuVO> menuList= roleAuthService.searchRoleAuthTree(operatorId,roleId,companyId);
        return menuList;
    }

    @Override
    public List<OrganizationVO> searchOrgAuthTree(Integer operatorId, Integer roleId, Integer archiveId) {
        List<OrganizationVO> organizationList = roleAuthService.searchOrgAuthTree(operatorId, roleId, archiveId);
        return organizationList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int roleRecoveryByArchiveId(List<Integer> idList, Integer operatorId) {

        if(null == operatorId || CollectionUtils.isEmpty(idList)){
            return 0;
        }

        int resultNumber = 0;
        RequestRoleVO userRole = new RequestRoleVO();
        userRole.setOperatorId(operatorId);
        for(Integer id : idList){
            userRole.setId(id);
            resultNumber += authHandoverDao.roleRecoveryByArchiveId(userRole);
            resultNumber += authHandoverDao.archiveRoleOrgRecovery(userRole);
        }
        return resultNumber;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public int roleHandoverByArchiveId(Integer acceptArchiveId, List<Integer> idList, Integer operatorId) {
        if(null == acceptArchiveId || null == operatorId || CollectionUtils.isEmpty(idList)){
            return 0;
        }

        int resultNumber = 0;
        RequestRoleVO userRole = new RequestRoleVO();
        userRole.setAcceptArchiveId(acceptArchiveId);
        userRole.setOperatorId(operatorId);
        for(Integer id : idList){
            userRole.setId(id);
            Integer acceptArchiveRoleId = authHandoverDao.searchAcceptArchiveAndId(userRole);
            if(acceptArchiveRoleId == null){
                resultNumber += authHandoverDao.roleHandoverByArchiveId(userRole);
            }
            resultNumber += authHandoverDao.archiveRoleOrgHandover(userRole);
        }
        return resultNumber;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public int roleTrusteeshipByArchiveId(Integer trusteeshipArchiveId, Integer acceptArchiveId, Date trusteeshipBeginTime, Date trusteeshipEndTime, List<Integer> idList, Integer operatorId) {
        if(null == trusteeshipArchiveId || null == acceptArchiveId || null == operatorId || CollectionUtils.isEmpty(idList)){
            return 0;
        }

        int resultNumber = 0;
        RequestRoleVO userRole = new RequestRoleVO();
        userRole.setTrusteeshipArchiveId(trusteeshipArchiveId);
        userRole.setAcceptArchiveId(acceptArchiveId);
        userRole.setIsTrusteeship(1);
        userRole.setTrusteeshipBeginTime(trusteeshipBeginTime);
        userRole.setTrusteeshipEndTime(trusteeshipEndTime);
        userRole.setOperatorId(operatorId);
        for(Integer id : idList){
            userRole.setId(id);
            resultNumber += authHandoverDao.roleTrusteeshipByArchiveId(userRole);
            resultNumber += authHandoverDao.archiveRoleOrgTrusteeship(userRole);
        }
        return resultNumber;
    }

    @Override
    public List<UserRoleVO> searchRoleListByArchiveId(Integer archiveId, Integer companyId) {
        if(null == archiveId){
            return null;
        }
        List<UserRoleVO> roleList = authHandoverDao.searchRoleListByArchiveId(archiveId,companyId);
        return roleList;
    }
}
