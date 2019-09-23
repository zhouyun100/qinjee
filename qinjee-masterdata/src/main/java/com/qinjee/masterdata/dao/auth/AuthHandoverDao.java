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

import com.qinjee.masterdata.model.vo.auth.MenuVO;
import com.qinjee.masterdata.model.vo.auth.OrganizationVO;
import com.qinjee.masterdata.model.vo.auth.RequestRoleVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限移交
 * @author 周赟
 * @date 2019/9/20
 */
@Service
public interface AuthHandoverDao {
    /**
     * 查询角色功能权限列表
     * @param roleId
     * @param companyId
     * @return
     */
    List<MenuVO> searchRoleAuthTree(Integer roleId, Integer companyId);

    /**
     * 查询角色机构权限列表
     * @param roleId
     * @param companyId
     * @return
     */
    List<OrganizationVO> searchOrgAuthTree(Integer roleId, Integer companyId);

    /**
     * 角色回收
     * @param userRole
     * @return
     */
    int roleRecoveryByArchiveId(RequestRoleVO userRole);

    /**
     * 机构回收
     * @param userRole
     * @return
     */
    int orgRecoveryByArchiveId(RequestRoleVO userRole);

    /**
     * 角色移交
     * @param userRole
     * @return
     */
    int roleHandoverByArchiveId(RequestRoleVO userRole);

    /**
     * 机构移交
     * @param userRole
     * @return
     */
    int orgHandoverByArchiveId(RequestRoleVO userRole);

    /**
     * 角色托管
     * @param userRole
     * @return
     */
    int roleTrusteeshipByArchiveId(RequestRoleVO userRole);

    /**
     * 机构托管
     * @param userRole
     * @return
     */
    int orgTrusteeshipByArchiveId(RequestRoleVO userRole);
}
