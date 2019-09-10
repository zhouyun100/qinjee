package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.TPositionLevel;

public interface TPositionLevelDao {
    int deleteByPrimaryKey(Integer positionLevelId);

    int insert(TPositionLevel record);

    int insertSelective(TPositionLevel record);

    TPositionLevel selectByPrimaryKey(Integer positionLevelId);

    int updateByPrimaryKeySelective(TPositionLevel record);

    int updateByPrimaryKey(TPositionLevel record);
}