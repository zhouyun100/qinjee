/**
 * 文件名：AuthHandoverService
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/20
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.auth;

import com.qinjee.masterdata.model.vo.auth.MenuVO;
import com.qinjee.masterdata.model.vo.auth.OrganizationVO;

import java.util.Date;
import java.util.List;

/**
 * 权限移交
 * @author 周赟
 * @date 2019/9/20
 */
public interface AuthHandoverService {

    /**
     * 查询角色功能权限树
     * @param roleId
     * @param companyId
     * @return
     */
    List<MenuVO> searchRoleAuthTree(Integer roleId,Integer companyId);

    /**
     * 查询角色机构权限树
     * @param roleId
     * @param companyId
     * @return
     */
    List<OrganizationVO> searchOrgAuthTree(Integer roleId,Integer companyId);

    /**
     * 角色回收
     * @param archiveId
     * @param roleIdList
     * @param operatorId
     * @return
     */
    int roleRecoveryByArchiveId(Integer archiveId, List<Integer> roleIdList,Integer operatorId);

    /**
     * 机构回收
     * @param archiveId
     * @param orgIdList
     * @param operatorId
     * @return
     */
    int orgRecoveryByArchiveId(Integer archiveId, List<Integer> orgIdList,Integer operatorId);

    /**
     * 角色移交
     * @param handoverArchiveId
     * @param acceptArchiveId
     * @param roleIdList
     * @param operatorId
     * @return
     */
    int roleHandoverByArchiveId(Integer handoverArchiveId, Integer acceptArchiveId, List<Integer> roleIdList,Integer operatorId);

    /**
     * 机构移交
     * @param handoverArchiveId
     * @param acceptArchiveId
     * @param orgIdList
     * @param operatorId
     * @return
     */
    int orgHandoverByArchiveId(Integer handoverArchiveId, Integer acceptArchiveId, List<Integer> orgIdList,Integer operatorId);

    /**
     * 角色托管
     * @param trusteeshipArchiveId
     * @param acceptArchiveId
     * @param trusteeshipBeginTime
     * @param trusteeshipEndTime
     * @param roleIdList
     * @param operatorId
     * @return
     */
    int roleTrusteeshipByArchiveId(Integer trusteeshipArchiveId, Integer acceptArchiveId, Date trusteeshipBeginTime, Date trusteeshipEndTime, List<Integer> roleIdList, Integer operatorId);

    /**
     * 机构托管
     * @param trusteeshipArchiveId
     * @param acceptArchiveId
     * @param trusteeshipBeginTime
     * @param trusteeshipEndTime
     * @param orgIdList
     * @param operatorId
     * @return
     */
    int orgTrusteeshipByArchiveId(Integer trusteeshipArchiveId, Integer acceptArchiveId, Date trusteeshipBeginTime, Date trusteeshipEndTime, List<Integer> orgIdList,Integer operatorId);
}
