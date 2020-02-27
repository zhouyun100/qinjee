/**
 * 文件名：AuthHandoverDao
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/20
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.dao.auth;

import com.qinjee.masterdata.model.vo.auth.RequestRoleVO;
import com.qinjee.masterdata.model.vo.auth.RoleGroupVO;
import com.qinjee.masterdata.model.vo.auth.UserRoleVO;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限移交
 * @author 周赟
 * @date 2019/9/20
 */
@Repository
public interface AuthHandoverDao {

    /**
     * 角色回收
     * @param userRole
     * @return
     */
    int roleRecoveryByArchiveId(RequestRoleVO userRole);

    /**
     * 用户角色机构回收机构回收
     * @param userRole
     * @return
     */
    int archiveRoleOrgRecovery(RequestRoleVO userRole);


    /**
     * 根据档案ID查询角色和角色组列表
     * @param archiveId
     * @param companyId
     * @return
     */
    List<UserRoleVO> searchRoleListByArchiveId(Integer archiveId, Integer companyId);

    /**
     * 角色移交
     * @param userRole
     * @return
     */
    int roleHandoverByArchiveId(RequestRoleVO userRole);

    /**
     * 用户角色查询
     * @param userRole
     * @return
     */
    Integer searchAcceptArchiveAndId(RequestRoleVO userRole);

    /**
     * 用户角色机构移交
     * @param userRole
     * @return
     */
    int archiveRoleOrgHandover(RequestRoleVO userRole);

    /**
     * 角色托管
     * @param userRole
     * @return
     */
    int roleTrusteeshipByArchiveId(RequestRoleVO userRole);

    /**
     * 用户角色机构托管
     * @param userRole
     * @return
     */
    int archiveRoleOrgTrusteeship(RequestRoleVO userRole);
}
