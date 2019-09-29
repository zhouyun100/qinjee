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
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

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
