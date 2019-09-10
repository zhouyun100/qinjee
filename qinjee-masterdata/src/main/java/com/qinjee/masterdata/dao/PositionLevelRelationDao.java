package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.PositionLevelRelation;

public interface PositionLevelRelationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PositionLevelRelation record);

    int insertSelective(PositionLevelRelation record);

    PositionLevelRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PositionLevelRelation record);

    int updateByPrimaryKey(PositionLevelRelation record);
}
