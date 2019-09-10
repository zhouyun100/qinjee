package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.OrganationHistory;

public interface OrganationHistoryDao {
    int deleteByPrimaryKey(Integer orgId);

    int insert(OrganationHistory record);

    int insertSelective(OrganationHistory record);

    OrganationHistory selectByPrimaryKey(Integer orgId);

    int updateByPrimaryKeySelective(OrganationHistory record);

    int updateByPrimaryKey(OrganationHistory record);
}