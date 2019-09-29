/**
 * 文件名：ArchiveAuthDao
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/18
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.dao.auth;

import com.qinjee.masterdata.model.entity.UserRole;
import com.qinjee.masterdata.model.vo.auth.ArchiveInfoVO;
import com.qinjee.masterdata.model.vo.auth.RoleGroupVO;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户授权
 * @author 周赟
 * @date 2019/9/18
 */
@Repository
public interface ArchiveAuthDao {

    /**
     * 根据角色ID查询用户列表
     * @param roleID
     * @return
     */
    List<ArchiveInfoVO> searchArchiveListByRoleId(Integer roleID);

    /**
     * 角色新增员工
     * @param userRole
     * @return
     */
    int addArchiveRole(UserRole userRole);

    /**
     * 角色移除员工
     * @param userRole
     * @return
     */
    int delArchiveRole(UserRole userRole);
}
