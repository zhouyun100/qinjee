package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.CustomOrgFieldCheck;

public interface CustomOrgFieldCheckDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomOrgFieldCheck record);

    int insertSelective(CustomOrgFieldCheck record);

    CustomOrgFieldCheck selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomOrgFieldCheck record);

    int updateByPrimaryKey(CustomOrgFieldCheck record);
}
