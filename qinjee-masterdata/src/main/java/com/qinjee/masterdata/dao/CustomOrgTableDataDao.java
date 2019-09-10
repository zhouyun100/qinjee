package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.CustomOrgTableData;

public interface CustomOrgTableDataDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomOrgTableData record);

    int insertSelective(CustomOrgTableData record);

    CustomOrgTableData selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomOrgTableData record);

    int updateByPrimaryKeyWithBLOBs(CustomOrgTableData record);

    int updateByPrimaryKey(CustomOrgTableData record);
}