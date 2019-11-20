/**
 * 文件名：ArchiveAuthServiceImpl
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/18
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.auth.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.auth.ArchiveAuthDao;
import com.qinjee.masterdata.model.entity.UserRole;
import com.qinjee.masterdata.model.vo.auth.ArchiveInfoVO;
import com.qinjee.masterdata.model.vo.auth.OrganizationArchiveVO;
import com.qinjee.masterdata.model.vo.auth.RoleGroupVO;
import com.qinjee.masterdata.service.auth.ArchiveAuthService;
import com.qinjee.masterdata.service.auth.RoleAuthService;
import com.qinjee.model.response.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 周赟
 * @date 2019/9/18
 */
@Service
public class ArchiveAuthServiceImpl implements ArchiveAuthService {

    @Autowired
    private ArchiveAuthDao archiveAuthDao;

    @Autowired
    private RoleAuthService roleAuthService;


    @Override
    public List<RoleGroupVO> searchRoleTree(Integer companyId) {
        List<RoleGroupVO> roleGroupList = roleAuthService.searchRoleTree(companyId);
        return roleGroupList;
    }

    @Override
    public void addArchiveRole(Integer roleId, List<Integer> archiveIdList,Integer operatorId) {

        if(!CollectionUtils.isEmpty(archiveIdList)){
            UserRole userRole;
            for(Integer archiveId : archiveIdList){
                userRole = new UserRole();
                userRole.setArchiveId(archiveId);
                userRole.setRoleId(roleId);
                userRole.setOperatorId(operatorId);
                archiveAuthDao.addArchiveRole(userRole);
            }
        }
    }

    @Override
    public void delArchiveRole(Integer roleId, List<Integer> archiveIdList,Integer operatorId) {
        if(!CollectionUtils.isEmpty(archiveIdList)){
            UserRole userRole;
            for(Integer archiveId : archiveIdList){
                userRole = new UserRole();
                userRole.setArchiveId(archiveId);
                userRole.setRoleId(roleId);
                userRole.setOperatorId(operatorId);
                archiveAuthDao.delArchiveRole(userRole);
            }
        }
    }

    @Override
    public List<OrganizationArchiveVO> getOrganizationArchiveTreeByArchiveId(Integer companyId, Integer archiveId) {

        List<OrganizationArchiveVO> organizationList = archiveAuthDao.searchOrganizationListByArchiveId(archiveId,new Date());
        List<ArchiveInfoVO> archiveInfoList = archiveAuthDao.searchArchiveListByCompanyId(companyId);
        //获取第一级机构
        List<OrganizationArchiveVO> organizationArchiveList = organizationList.stream().filter(organization -> {
            if (organization.getOrgParentId() != null && organization.getOrgParentId() == 0) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());

        //移除一级机构
        organizationList.removeAll(organizationArchiveList);

        //递归处理机构,使其以树形结构展示
        handlerOrganizationArchiveToTree(archiveInfoList,organizationList, organizationArchiveList);

        return organizationArchiveList;
    }

    private void handlerOrganizationArchiveToTree(List<ArchiveInfoVO> archiveInfoList, List<OrganizationArchiveVO> organizationList, List<OrganizationArchiveVO> organizationArchiveList) {
        for (OrganizationArchiveVO orgArchive : organizationArchiveList) {

            //筛选子集机构
            List<OrganizationArchiveVO> childOrgList = organizationList.stream().filter(organization -> {
                if (organization.getOrgParentId().equals(orgArchive.getOrgId())) {
                    return true;
                }else{
                    return false;
                }
            }).collect(Collectors.toList());

            //筛选子集人员
            List<ArchiveInfoVO> childArchiveList = archiveInfoList.stream().filter(archiveInfoVO -> {
                if(archiveInfoVO.getOrgId().equals(orgArchive.getOrgId())){
                    return true;
                }else{
                    return false;
                }
            }).collect(Collectors.toList());

            if(!CollectionUtils.isEmpty(childArchiveList)){
                orgArchive.setChildArchiveList(childArchiveList);
                archiveInfoList.removeAll(childArchiveList);
            }

            //判断是否还有子级机构
            if (!CollectionUtils.isEmpty(childOrgList)) {
                orgArchive.setChildOrgList(childOrgList);
                organizationList.removeAll(childOrgList);
                handlerOrganizationArchiveToTree(archiveInfoList,organizationList, childOrgList);
            }
        }
    }
}
