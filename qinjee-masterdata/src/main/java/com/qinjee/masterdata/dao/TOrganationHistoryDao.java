package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.TOrganationHistory;

public interface TOrganationHistoryDao {
    int deleteByPrimaryKey(Integer orgId);

    int insert(TOrganationHistory record);

    int insertSelective(TOrganationHistory record);

    TOrganationHistory selectByPrimaryKey(Integer orgId);

    int updateByPrimaryKeySelective(TOrganationHistory record);

    int updateByPrimaryKey(TOrganationHistory record);
}