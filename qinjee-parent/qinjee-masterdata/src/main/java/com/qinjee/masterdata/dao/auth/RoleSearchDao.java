/**
 * 文件名：RoleSearchDao
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/19
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.dao.auth;

import com.qinjee.masterdata.model.entity.UserRole;
import com.qinjee.masterdata.model.vo.auth.ArchiveInfoVO;
import com.qinjee.masterdata.model.vo.auth.RequestArchivePageVO;
import com.qinjee.masterdata.model.vo.auth.RoleGroupVO;
import com.qinjee.masterdata.model.vo.auth.UserRoleVO;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色反查
 * @author 周赟
 * @date 2019/9/18
 */
@Repository
public interface RoleSearchDao {

    /**
     * 根据工号或姓名模糊查询员工列表
     * @param archivePageVO
     * @return
     */
    List<ArchiveInfoVO> searchArchiveListByUserName(RequestArchivePageVO archivePageVO);

    /**
     * 根据档案ID查询角色和角色组列表
     * @param archiveId
     * @param companyId
     * @return
     */
    List<RoleGroupVO> searchRoleListByArchiveId(Integer archiveId, Integer companyId);

    /**
     * 添加档案角色
     * @param userRole
     * @return
     */
    int insertUserRole(UserRole userRole);

    /**
     * 删除档案角色
     * @param userRole
     * @return
     */
    int delUserRole(UserRole userRole);

}
