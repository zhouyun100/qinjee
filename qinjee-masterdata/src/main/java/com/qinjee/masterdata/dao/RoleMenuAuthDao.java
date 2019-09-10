package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.RoleMenuAuth;

public interface RoleMenuAuthDao {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleMenuAuth record);

    int insertSelective(RoleMenuAuth record);

    RoleMenuAuth selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleMenuAuth record);

    int updateByPrimaryKey(RoleMenuAuth record);
}
