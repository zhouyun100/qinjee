package com.qinjee.masterdata.dao;


import com.qinjee.masterdata.model.entity.Organization;

public interface OrganizationDao {
    int deleteByPrimaryKey(Integer orgId);

    int insert(Organization record);

    int insertSelective(Organization record);

    Organization selectByPrimaryKey(Integer orgId);

    int updateByPrimaryKeySelective(Organization record);

    int updateByPrimaryKey(Organization record);

}
