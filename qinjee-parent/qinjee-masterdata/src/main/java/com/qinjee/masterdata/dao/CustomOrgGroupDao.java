package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.CustomOrgGroup;

public interface CustomOrgGroupDao {
    int deleteByPrimaryKey(Integer groupId);

    int insert(CustomOrgGroup record);

    int insertSelective(CustomOrgGroup record);

    CustomOrgGroup selectByPrimaryKey(Integer groupId);

    int updateByPrimaryKeySelective(CustomOrgGroup record);

    int updateByPrimaryKey(CustomOrgGroup record);
}
