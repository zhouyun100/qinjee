package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.RoleOrgAuth;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleOrgAuthDao {
    int deleteByPrimaryKey(Integer authId);

    int insert(RoleOrgAuth record);

    int insertSelective(RoleOrgAuth record);

    RoleOrgAuth selectByPrimaryKey(Integer authId);

    int updateByPrimaryKeySelective(RoleOrgAuth record);

    int updateByPrimaryKey(RoleOrgAuth record);

}
