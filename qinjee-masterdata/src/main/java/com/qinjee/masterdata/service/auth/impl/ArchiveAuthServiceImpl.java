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

import com.qinjee.masterdata.dao.auth.ArchiveAuthDao;
import com.qinjee.masterdata.model.entity.UserRole;
import com.qinjee.masterdata.model.vo.auth.ArchiveInfoVO;
import com.qinjee.masterdata.model.vo.auth.RoleGroupVO;
import com.qinjee.masterdata.service.auth.ArchiveAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author 周赟
 * @date 2019/9/18
 */
@Service
public class ArchiveAuthServiceImpl implements ArchiveAuthService {

    @Autowired
    private ArchiveAuthDao archiveAuthDao;

    @Override
    // TODO 根据企业ID查询角色树
    public List<RoleGroupVO> searchRoleTree(Integer companyId) {
        archiveAuthDao.searchRoleTree(companyId);
        return null;
    }

    @Override
    public List<ArchiveInfoVO> searchArchiveListByRoleId(Integer roleID) {
        List<ArchiveInfoVO> archiveInfoVOList = null;
        if(null != roleID){
            archiveInfoVOList = archiveAuthDao.searchArchiveListByRoleId(roleID);
        }
        return archiveInfoVOList;
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
}
