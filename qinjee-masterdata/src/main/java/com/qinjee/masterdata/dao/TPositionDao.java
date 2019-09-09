package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.TPosition;

public interface TPositionDao {
    int deleteByPrimaryKey(Integer positionId);

    int insert(TPosition record);

    int insertSelective(TPosition record);

    TPosition selectByPrimaryKey(Integer positionId);

    int updateByPrimaryKeySelective(TPosition record);

    int updateByPrimaryKey(TPosition record);
}