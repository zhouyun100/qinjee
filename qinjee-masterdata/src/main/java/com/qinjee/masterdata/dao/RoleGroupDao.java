package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.RoleGroup;

public interface RoleGroupDao {
    int deleteByPrimaryKey(Integer roleGroupId);

    int insert(RoleGroup record);

    int insertSelective(RoleGroup record);

    RoleGroup selectByPrimaryKey(Integer roleGroupId);

    int updateByPrimaryKeySelective(RoleGroup record);

    int updateByPrimaryKey(RoleGroup record);
}