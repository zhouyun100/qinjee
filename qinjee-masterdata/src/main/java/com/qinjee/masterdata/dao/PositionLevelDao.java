package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.PositionLevel;

public interface PositionLevelDao {
    int deleteByPrimaryKey(Integer positionLevelId);

    int insert(PositionLevel record);

    int insertSelective(PositionLevel record);

    PositionLevel selectByPrimaryKey(Integer positionLevelId);

    int updateByPrimaryKeySelective(PositionLevel record);

    int updateByPrimaryKey(PositionLevel record);
}