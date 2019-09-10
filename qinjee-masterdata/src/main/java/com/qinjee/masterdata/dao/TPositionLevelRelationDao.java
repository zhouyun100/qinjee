package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.TPositionLevelRelation;

public interface TPositionLevelRelationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TPositionLevelRelation record);

    int insertSelective(TPositionLevelRelation record);

    TPositionLevelRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TPositionLevelRelation record);

    int updateByPrimaryKey(TPositionLevelRelation record);
}