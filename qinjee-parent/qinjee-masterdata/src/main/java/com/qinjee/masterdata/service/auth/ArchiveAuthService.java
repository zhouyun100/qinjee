/**
 * 文件名：ArchiveAuthService
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/18
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.auth;

import com.qinjee.masterdata.model.entity.Organization;
import com.qinjee.masterdata.model.vo.auth.ArchiveInfoVO;
import com.qinjee.masterdata.model.vo.auth.OrganizationArchiveVO;
import com.qinjee.masterdata.model.vo.auth.RoleGroupVO;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;

import java.util.List;

/**
 * 用户授权
 * @author 周赟
 * @date 2019/9/18
 */
public interface ArchiveAuthService {

    /**
     * 根据企业ID查询角色树
     * @param companyId
     * @return
     */
    List<RoleGroupVO> searchRoleTree(Integer companyId);


    /**
     * 角色新增员工
     * @param roleId
     * @param archiveIdList
     * @param operatorId
     * @return
     */
    void addArchiveRole(Integer roleId, List<Integer> archiveIdList, Integer operatorId);

    /**
     * 角色移除员工
     * @param roleId
     * @param archiveIdList
     * @param operatorId
     * @return
     */
    void delArchiveRole(Integer roleId, List<Integer> archiveIdList, Integer operatorId);

    /**
     * 查询当前机构员工树
     * @param companyId
     * @param archiveId
     * @return
     */
    List<OrganizationArchiveVO> getOrganizationArchiveTreeByArchiveId(Integer companyId,Integer archiveId);
}
