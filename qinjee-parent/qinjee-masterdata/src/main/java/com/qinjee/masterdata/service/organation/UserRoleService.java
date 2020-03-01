package com.qinjee.masterdata.service.organation;

import com.qinjee.masterdata.model.entity.UserRole;

import java.util.List;

/**
 * @author 彭洪思
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月17日 17:53:00
 */
public interface UserRoleService {
    /**
     * 根据档案id查询档案对应的所有有效的角色
     * @param archiveId
     * @return
     */
    List<UserRole> getUserRoleList(Integer archiveId);
}
