/**
 * 文件名：RoleSearchService
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/19
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.service.auth;

import com.qinjee.masterdata.model.vo.auth.ArchiveInfoVO;
import com.qinjee.masterdata.model.vo.auth.UserRoleVO;

import java.util.List;

/**
 * 角色反查
 * @author 周赟
 * @date 2019/9/18
 */
public interface RoleSearchService {

    /**
     * 根据工号或姓名模糊查询员工列表
     * @param userName
     * @param companyId
     * @return
     */
    List<ArchiveInfoVO> searchArchiveListByUserName(String userName,Integer companyId);

    /**
     * 根据档案ID查询角色列表
     * @param archiveId
     * @return
     */
    List<UserRoleVO> searchRoleListByArchiveId(Integer archiveId, Integer companyId);

    /**
     * 更新员工角色
     * @param archiveId 员工ID
     * @param roleIdList 角色ID
     * @param operatorId 操作人档案ID
     */
    void updateArchiveRole(Integer archiveId, List<Integer> roleIdList,Integer operatorId);
}
