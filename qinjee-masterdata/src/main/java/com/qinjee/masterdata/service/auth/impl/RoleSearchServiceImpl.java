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

import com.qinjee.masterdata.dao.auth.RoleSearchDao;
import com.qinjee.masterdata.model.entity.UserRole;
import com.qinjee.masterdata.model.vo.auth.ArchiveInfoVO;
import com.qinjee.masterdata.model.vo.auth.UserRoleVO;
import com.qinjee.masterdata.service.auth.RoleSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author 周赟
 * @date 2019/9/18
 */
@Service
public class RoleSearchServiceImpl implements RoleSearchService {

    @Autowired
    private RoleSearchDao roleSearchDao;

    @Override
    public List<ArchiveInfoVO> searchArchiveListByUserName(String userName,Integer companyId) {
        if(null == companyId){
            return null;
        }
        List<ArchiveInfoVO> archiveInfoList = roleSearchDao.searchArchiveListByUserName(userName,companyId);
        return archiveInfoList;
    }

    @Override
    public List<UserRoleVO> searchRoleListByArchiveId(Integer archiveId, Integer companyId) {
        if(null == archiveId || null == companyId){
            return null;
        }
        List<UserRoleVO> roleList = roleSearchDao.searchRoleListByArchiveId(archiveId,companyId);
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

        if(!CollectionUtils.isEmpty(roleIdList)){
            for (Integer roleId : roleIdList){
                userRole.setRoleId(roleId);
                roleSearchDao.insertUserRole(userRole);
            }

        }
    }
}
