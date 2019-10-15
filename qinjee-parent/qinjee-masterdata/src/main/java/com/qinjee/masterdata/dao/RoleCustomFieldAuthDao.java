package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.RoleCustomFieldAuth;

public interface RoleCustomFieldAuthDao {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleCustomFieldAuth record);

    int insertSelective(RoleCustomFieldAuth record);

    RoleCustomFieldAuth selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleCustomFieldAuth record);

    int updateByPrimaryKey(RoleCustomFieldAuth record);
}
