package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.Position;

public interface PositionDao {
    int deleteByPrimaryKey(Integer positionId);

    int insert(Position record);

    int insertSelective(Position record);

    Position selectByPrimaryKey(Integer positionId);

    int updateByPrimaryKeySelective(Position record);

    int updateByPrimaryKey(Position record);
}
