package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.TOrganation;

public interface TOrganationDao {
    int deleteByPrimaryKey(Integer orgId);

    int insert(TOrganation record);

    int insertSelective(TOrganation record);

    TOrganation selectByPrimaryKey(Integer orgId);

    int updateByPrimaryKeySelective(TOrganation record);

    int updateByPrimaryKey(TOrganation record);
}