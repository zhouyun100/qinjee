package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.TCustomOrgTableData;

public interface TCustomOrgTableDataDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TCustomOrgTableData record);

    int insertSelective(TCustomOrgTableData record);

    TCustomOrgTableData selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TCustomOrgTableData record);

    int updateByPrimaryKeyWithBLOBs(TCustomOrgTableData record);

    int updateByPrimaryKey(TCustomOrgTableData record);
}