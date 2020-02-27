package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.CustomOrgTable;

public interface CustomOrgTableDao {
    int deleteByPrimaryKey(Integer tableId);

    int insert(CustomOrgTable record);

    int insertSelective(CustomOrgTable record);

    CustomOrgTable selectByPrimaryKey(Integer tableId);

    int updateByPrimaryKeySelective(CustomOrgTable record);

    int updateByPrimaryKey(CustomOrgTable record);
}
