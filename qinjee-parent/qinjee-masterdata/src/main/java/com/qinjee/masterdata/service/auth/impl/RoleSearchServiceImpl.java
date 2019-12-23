/**
 * 文件名：RoleSearchServiceImpl
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/19
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.auth.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.auth.RoleSearchDao;
import com.qinjee.masterdata.model.entity.Organization;
import com.qinjee.masterdata.model.entity.UserRole;
import com.qinjee.masterdata.model.vo.auth.ArchiveInfoVO;
import com.qinjee.masterdata.model.vo.auth.RequestArchivePageVO;
import com.qinjee.masterdata.model.vo.auth.RoleGroupVO;
import com.qinjee.masterdata.service.auth.RoleAuthService;
import com.qinjee.masterdata.service.auth.RoleSearchService;
import com.qinjee.model.response.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 周赟
 * @date 2019/9/18
 */
@Service
public class RoleSearchServiceImpl implements RoleSearchService {

    @Autowired
    private RoleSearchDao roleSearchDao;

    @Autowired
    private RoleAuthService roleAuthService;

    @Override
    public PageResult<ArchiveInfoVO> searchArchiveListByUserName(RequestArchivePageVO archivePageVO,Integer operatorId) {
        if(null == archivePageVO || null == archivePageVO.getCompanyId()){
            return null;
        }
        if(archivePageVO.getCurrentPage() != null && archivePageVO.getPageSize() != null){
            PageHelper.startPage(archivePageVO.getCurrentPage(),archivePageVO.getPageSize());
        }
        archivePageVO.setCurrentDateTime(new Date());

        List<Integer> orgIdList = null;
        Integer orgId = archivePageVO.getOrgId();
        if(orgId != null){
            orgIdList = new ArrayList<>();
            orgIdList.add(orgId);
            List<Organization> orgIdAllList = roleSearchDao.getOrganizationListByArchiveId(operatorId,new Date());
            handlerOrgIdSonList(orgIdList, orgId, orgIdAllList);
        }
        List<ArchiveInfoVO> archiveInfoList = roleSearchDao.searchArchiveListByUserName(archivePageVO,orgIdList);
        PageResult<ArchiveInfoVO> pageResult = new PageResult<>(archiveInfoList);
        return pageResult;
    }

    private void handlerOrgIdSonList(List<Integer> orgIdList,Integer parentOrgId, List<Organization> orgIdAllList){

        List<Organization> orgIdSonList = orgIdAllList.stream().filter(organization ->{
            if(organization.getOrgParentId().equals(parentOrgId)){
                orgIdList.add(organization.getOrgId());
                return true;
            }else{
                return false;
            }
        }).collect(Collectors.toList());

        for(Organization organization : orgIdSonList){
            handlerOrgIdSonList(orgIdList,organization.getOrgId(),orgIdAllList);
        }
    }

    @Override
    public List<RoleGroupVO> searchRoleTreeByArchiveId(Integer archiveId, Integer companyId) {
        if(null == archiveId || null == companyId){
            return null;
        }
        List<RoleGroupVO> roleList = roleSearchDao.searchRoleListByArchiveId(archiveId,companyId);

        /**
         * 如果角色列表为空则直接返回null
         */
        if(CollectionUtils.isNotEmpty(roleList)){
            /**
             * 提取当前角色树的一级节点
             */
            List<RoleGroupVO> firstRoleList = roleList.stream().filter(roleGroupVO -> {
                if(roleGroupVO.getParentRoleGroupId() == null || roleGroupVO.getParentRoleGroupId() == 0){
                    return true;
                }else{
                    return false;
                }
            }).collect(Collectors.toList());

            roleAuthService.handlerRoleToTree(roleList,firstRoleList);
        }else{
            return null;
        }
        return roleList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateArchiveRole(Integer archiveId, List<Integer> roleIdList,Integer operatorId) {
        if(null == archiveId || null == operatorId){
            return;
        }

        UserRole userRole = new UserRole();
        userRole.setArchiveId(archiveId);
        userRole.setOperatorId(operatorId);
        roleSearchDao.delUserRole(userRole);

        if(CollectionUtils.isNotEmpty(roleIdList)){
            for (Integer roleId : roleIdList){
                userRole.setRoleId(roleId);
                roleSearchDao.insertUserRole(userRole);
            }

        }
    }
}
