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
import com.qinjee.masterdata.service.auth.ApiAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    @Override
    public int mergeOrg(List<Integer> oldOrgIdList, Integer newOrgId, Integer operatorId) {
        int resultNum = 0;
        if(CollectionUtils.isEmpty(oldOrgIdList)){
            return 0;
        }
        List<Role> roleList = apiAuthDao.searchParentOrgIdAutoAuthChildOrgRoleList(newOrgId);

        if(!CollectionUtils.isEmpty(roleList)){

            //TODO 根据旧机构ID获取所有子集机构ID列表

            List<Integer> delOrgIdList = oldOrgIdList.stream().filter(oldOrgId ->{
                if(oldOrgId.equals(newOrgId)){
                    return false;
                }else{
                    return true;
                }
            }).collect(Collectors.toList());

            if(CollectionUtils.isEmpty(roleList)){
                for(Role role : roleList){
                    resultNum += apiAuthDao.insertRoleOrg(newOrgId,role.getRoleId(),operatorId);
                    for(Integer orgId : delOrgIdList){
                        resultNum += apiAuthDao.deleteRoleOrgByRoleIdAndOrgId(orgId,role.getRoleId(),operatorId);
                    }
                }
            }
        }


        return resultNum;
    }

    @Override
    public int transferOrg(List<Integer> orgIdList, Integer parentOrgId, Integer operatorId) {
        int resultNum = 0;
        if(CollectionUtils.isEmpty(orgIdList)){
            return 0;
        }
        List<Role> roleList = apiAuthDao.searchAutoAuthChildOrgRoleList(parentOrgId);

        if(CollectionUtils.isEmpty(roleList)){

            //TODO 根据划转机构列表获取所有子集机构ID列表

            for(Role role : roleList){
                for(Integer orgId : orgIdList){
                    resultNum += apiAuthDao.insertRoleOrg(orgId,role.getRoleId(),operatorId);
                }
            }
        }
        return resultNum;
    }

    @Override
    public int addOrg(Integer orgId, Integer parentOrgId, Integer operatorId) {
        int resultNum = 0;
        List<Role> roleList = apiAuthDao.searchAutoAuthChildOrgRoleList(parentOrgId);

        if(CollectionUtils.isEmpty(roleList)){
            for(Role role : roleList){
                resultNum += apiAuthDao.insertRoleOrg(orgId,role.getRoleId(),operatorId);
            }
        }
        return resultNum;
    }

    @Override
    public int deleteOrg(List<Integer> orgIdList, Integer operatorId) {
        int resultNum = 0;
        for(Integer orgId : orgIdList){
            resultNum += apiAuthDao.deleteRoleOrgAuthByOrgId(orgId,operatorId);
            resultNum += apiAuthDao.deleteArchiveOrgAuth(orgId,operatorId);
        }
        return resultNum;
    }

    @Override
    public int deleteArchiveAuth(Integer archiveId, Integer operatorId) {
        int resultNum = 0;
        resultNum += apiAuthDao.deleteArchiveRoleAuth(archiveId, operatorId);
        resultNum += apiAuthDao.deleteArchiveOrgAuth(archiveId, operatorId);
        return resultNum;
    }
}
