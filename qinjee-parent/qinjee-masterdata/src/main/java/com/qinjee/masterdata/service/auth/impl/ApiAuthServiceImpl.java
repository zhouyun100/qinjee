/**
 * 文件名：ApiAuthServiceImpl
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/11/25
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.auth.impl;

import com.qinjee.masterdata.dao.auth.ApiAuthDao;
import com.qinjee.masterdata.model.entity.Role;
import com.qinjee.masterdata.model.vo.auth.UserRoleVO;
import com.qinjee.masterdata.service.auth.ApiAuthService;
import com.qinjee.masterdata.service.auth.AuthHandoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 周赟
 * @date 2019/11/25
 */
@Service
public class ApiAuthServiceImpl implements ApiAuthService {

    @Autowired
    private ApiAuthDao apiAuthDao;

    @Autowired
    private AuthHandoverService authHandoverService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int mergeOrg(List<Integer> oldOrgIdList, Integer newOrgId, Integer operatorId) {
        int resultNum = 0;
        if(CollectionUtils.isEmpty(oldOrgIdList)){
            return 0;
        }

        //根据新机构ID查询父级机构拥有自动授权子集机构权限的角色列表
        List<Role> roleList = apiAuthDao.searchParentOrgIdAutoAuthChildOrgRoleList(newOrgId);

        if(CollectionUtils.isNotEmpty(roleList)){

            //根据旧机构ID列表获取所有子集机构ID列表
            handlerAllChildOrganization(oldOrgIdList,oldOrgIdList);

            List<Integer> delOrgIdList = oldOrgIdList.stream().filter(oldOrgId ->{
                if(oldOrgId.equals(newOrgId)){
                    return false;
                }else{
                    return true;
                }
            }).collect(Collectors.toList());

            if(CollectionUtils.isNotEmpty(roleList)){
                for(Role role : roleList){
                    resultNum += apiAuthDao.insertRoleOrg(newOrgId,role.getRoleId(),operatorId);
                    resultNum += apiAuthDao.deleteRoleOrgAuth(delOrgIdList,role.getRoleId(),operatorId);
                }
            }
        }
        return resultNum;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int transferOrg(List<Integer> orgIdList, Integer parentOrgId, Integer operatorId) {
        int resultNum = 0;
        if(CollectionUtils.isNotEmpty(orgIdList)){
            return 0;
        }
        List<Role> roleList = apiAuthDao.searchAutoAuthChildOrgRoleList(parentOrgId);

        if(CollectionUtils.isNotEmpty(roleList)){

            //根据划转机构列表获取所有子集机构ID列表
            handlerAllChildOrganization(orgIdList,orgIdList);

            for(Role role : roleList){
                for(Integer orgId : orgIdList){
                    resultNum += apiAuthDao.insertRoleOrg(orgId,role.getRoleId(),operatorId);
                }
            }
        }
        return resultNum;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addOrg(Integer orgId, Integer parentOrgId, Integer operatorId) {
        int resultNum = 0;
        List<Role> roleList = apiAuthDao.searchAutoAuthChildOrgRoleList(parentOrgId);

        if(CollectionUtils.isNotEmpty(roleList)){
            for(Role role : roleList){
                resultNum += apiAuthDao.insertRoleOrg(orgId,role.getRoleId(),operatorId);
            }
        }else{

            /**
             * 新增的机构如何对应的上级机构角色没有自动授权子集机构权限，则默认给当前操作人所有角色添加机构人员角色权限
             */
            List<UserRoleVO> userRoleVOList = authHandoverService.searchRoleListByArchiveId(operatorId,null);
            for(UserRoleVO userRoleVO : userRoleVOList){
                resultNum += apiAuthDao.insertUserOrg(orgId,operatorId,userRoleVO.getRoleId(),operatorId);
            }

        }
        return resultNum;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteOrg(List<Integer> orgIdList, Integer operatorId) {
        int resultNum = 0;

        resultNum += apiAuthDao.deleteRoleOrgAuth(orgIdList, null, operatorId);
        for(Integer orgId : orgIdList){

            resultNum += apiAuthDao.deleteArchiveOrgAuth(null, orgId,operatorId);
        }
        return resultNum;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteArchiveAuth(Integer archiveId, Integer operatorId) {
        int resultNum = 0;
        resultNum += apiAuthDao.deleteArchiveRoleAuth(archiveId, operatorId);
        resultNum += apiAuthDao.deleteArchiveOrgAuth(archiveId, null, operatorId);
        return resultNum;
    }

    private void handlerAllChildOrganization(List<Integer> currentOrgIdList, List<Integer> allOrgIdList){

        List<Integer> childOrganizationList = apiAuthDao.searchChildOrganizationList(currentOrgIdList);
        if(CollectionUtils.isNotEmpty(childOrganizationList)){
            allOrgIdList.addAll(childOrganizationList);
            handlerAllChildOrganization(childOrganizationList,allOrgIdList);
        }

    }
}
