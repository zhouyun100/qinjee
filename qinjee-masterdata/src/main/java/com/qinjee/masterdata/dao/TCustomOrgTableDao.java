package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.TCustomOrgTable;

public interface TCustomOrgTableDao {
    int deleteByPrimaryKey(Integer tableId);

    int insert(TCustomOrgTable record);

    int insertSelective(TCustomOrgTable record);

    TCustomOrgTable selectByPrimaryKey(Integer tableId);

    int updateByPrimaryKeySelective(TCustomOrgTable record);

    int updateByPrimaryKey(TCustomOrgTable record);
}