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
import com.qinjee.masterdata.model.vo.auth.RequestUserRoleVO;
import com.qinjee.masterdata.service.auth.AuthHandoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    @Override
    // TODO 查询角色功能树
    public List<MenuVO> searchRoleAuthTree(Integer roleId, Integer companyId) {
        List<MenuVO> menuList= authHandoverDao.searchRoleAuthTree(roleId,companyId);
        return menuList;
    }

    @Override
    // TODO 查询角色机构树
    public List<OrganizationVO> searchOrgAuthTree(Integer roleId, Integer companyId) {
        List<OrganizationVO> organizationList = authHandoverDao.searchOrgAuthTree(roleId, companyId);
        return organizationList;
    }

    @Override
    public int roleRecoveryByArchiveId(Integer archiveId, List<Integer> roleIdList, Integer operatorId) {

        if(null == archiveId || null == operatorId || !CollectionUtils.isEmpty(roleIdList)){
            return 0;
        }

        int resultNumber = 0;
        int rowNumber;
        RequestUserRoleVO userRole = null;
        userRole.setArchiveId(archiveId);
        userRole.setOperatorId(operatorId);
        for(Integer roleId : roleIdList){
            userRole.setRoleId(roleId);
            rowNumber = authHandoverDao.roleRecoveryByArchiveId(userRole);
            resultNumber += rowNumber;
        }
        return resultNumber;
    }

    @Override
    public int orgRecoveryByArchiveId(Integer archiveId, List<Integer> orgIdList, Integer operatorId) {
        if(null == archiveId || null == operatorId || !CollectionUtils.isEmpty(orgIdList)){
            return 0;
        }

        int resultNumber = 0;
        int rowNumber;
        RequestUserRoleVO userRole = null;
        userRole.setArchiveId(archiveId);
        userRole.setOperatorId(operatorId);
        for(Integer orgId : orgIdList){
            userRole.setOrgId(orgId);
            rowNumber = authHandoverDao.orgRecoveryByArchiveId(userRole);
            resultNumber += rowNumber;
        }
        return resultNumber;
    }

    @Override
    public int roleHandoverByArchiveId(Integer handoverArchiveId, Integer acceptArchiveId, List<Integer> roleIdList, Integer operatorId) {
        if(null == handoverArchiveId || null == acceptArchiveId || null == operatorId || !CollectionUtils.isEmpty(roleIdList)){
            return 0;
        }

        int resultNumber = 0;
        int rowNumber;
        RequestUserRoleVO userRole = null;
        userRole.setHandoverArchiveId(handoverArchiveId);
        userRole.setAcceptArchiveId(acceptArchiveId);
        userRole.setOperatorId(operatorId);
        for(Integer roleId : roleIdList){
            userRole.setRoleId(roleId);
            rowNumber = authHandoverDao.roleHandoverByArchiveId(userRole);
            resultNumber += rowNumber;
        }
        return resultNumber;
    }

    @Override
    public int orgHandoverByArchiveId(Integer handoverArchiveId, Integer acceptArchiveId, List<Integer> orgIdList, Integer operatorId) {
        if(null == handoverArchiveId || null == acceptArchiveId || null == operatorId || !CollectionUtils.isEmpty(orgIdList)){
            return 0;
        }

        int resultNumber = 0;
        int rowNumber;
        RequestUserRoleVO userRole = null;
        userRole.setHandoverArchiveId(handoverArchiveId);
        userRole.setAcceptArchiveId(acceptArchiveId);
        userRole.setOperatorId(operatorId);
        for(Integer orgId : orgIdList){
            userRole.setOrgId(orgId);
            rowNumber = authHandoverDao.orgHandoverByArchiveId(userRole);
            resultNumber += rowNumber;
        }
        return resultNumber;
    }

    @Override
    public int roleTrusteeshipByArchiveId(Integer trusteeshipArchiveId, Integer acceptArchiveId, Date trusteeshipBeginTime, Date trusteeshipEndTime, List<Integer> roleIdList, Integer operatorId) {
        if(null == trusteeshipArchiveId || null == acceptArchiveId || null == operatorId || !CollectionUtils.isEmpty(roleIdList)){
            return 0;
        }

        int resultNumber = 0;
        int rowNumber;
        RequestUserRoleVO userRole = null;
        userRole.setHandoverArchiveId(trusteeshipArchiveId);
        userRole.setAcceptArchiveId(acceptArchiveId);
        userRole.setTrusteeshipBeginTime(trusteeshipBeginTime);
        userRole.setTrusteeshipEndTime(trusteeshipEndTime);
        userRole.setOperatorId(operatorId);
        for(Integer roleId : roleIdList){
            userRole.setRoleId(roleId);
            rowNumber = authHandoverDao.roleTrusteeshipByArchiveId(userRole);
            resultNumber += rowNumber;
        }
        return resultNumber;
    }

    @Override
    public int orgTrusteeshipByArchiveId(Integer trusteeshipArchiveId, Integer acceptArchiveId, Date trusteeshipBeginTime, Date trusteeshipEndTime, List<Integer> orgIdList, Integer operatorId) {
        if(null == trusteeshipArchiveId || null == acceptArchiveId || null == operatorId || !CollectionUtils.isEmpty(orgIdList)){
            return 0;
        }

        int resultNumber = 0;
        int rowNumber;
        RequestUserRoleVO userRole = null;
        userRole.setHandoverArchiveId(trusteeshipArchiveId);
        userRole.setAcceptArchiveId(acceptArchiveId);
        userRole.setTrusteeshipBeginTime(trusteeshipBeginTime);
        userRole.setTrusteeshipEndTime(trusteeshipEndTime);
        userRole.setOperatorId(operatorId);
        for(Integer orgId : orgIdList){
            userRole.setOrgId(orgId);
            rowNumber = authHandoverDao.orgTrusteeshipByArchiveId(userRole);
            resultNumber += rowNumber;
        }
        return resultNumber;
    }
}
