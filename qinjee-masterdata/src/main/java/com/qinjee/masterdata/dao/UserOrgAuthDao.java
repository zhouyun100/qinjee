package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.UserOrgAuth;

public interface UserOrgAuthDao {
    int deleteByPrimaryKey(Integer authId);

    int insert(UserOrgAuth record);

    int insertSelective(UserOrgAuth record);

    UserOrgAuth selectByPrimaryKey(Integer authId);

    int updateByPrimaryKeySelective(UserOrgAuth record);

    int updateByPrimaryKey(UserOrgAuth record);
}