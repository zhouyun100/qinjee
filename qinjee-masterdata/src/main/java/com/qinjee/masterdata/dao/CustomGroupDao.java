package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.CustomGroup;

public interface CustomGroupDao {
    int deleteByPrimaryKey(Integer groupId);

    int insert(CustomGroup record);

    int insertSelective(CustomGroup record);

    CustomGroup selectByPrimaryKey(Integer groupId);

    int updateByPrimaryKeySelective(CustomGroup record);

    int updateByPrimaryKey(CustomGroup record);
}
