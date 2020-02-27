package com.qinjee.masterdata.dao.organation;

import com.qinjee.masterdata.model.entity.UserRole;

import java.util.List;

public interface UserRoleDao {
    int deleteByPrimaryKey(Integer id);

    int insert(UserRole record);

    int insertSelective(UserRole record);

    UserRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserRole record);

    int updateByPrimaryKey(UserRole record);

    /**
     * 根据档案id查询未删除的角色
     * @param archiveId
     * @return
     */
    List<UserRole> getUserRoleList(Integer archiveId);
}
