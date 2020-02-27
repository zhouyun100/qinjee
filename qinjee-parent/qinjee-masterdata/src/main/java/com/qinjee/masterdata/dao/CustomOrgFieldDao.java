package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.CustomOrgField;

public interface CustomOrgFieldDao {
    int deleteByPrimaryKey(Integer fieldId);

    int insert(CustomOrgField record);

    int insertSelective(CustomOrgField record);

    CustomOrgField selectByPrimaryKey(Integer fieldId);

    int updateByPrimaryKeySelective(CustomOrgField record);

    int updateByPrimaryKey(CustomOrgField record);
}
