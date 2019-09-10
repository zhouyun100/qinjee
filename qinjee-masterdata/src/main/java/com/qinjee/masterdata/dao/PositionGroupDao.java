package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.PositionGroup;

public interface PositionGroupDao {
    int deleteByPrimaryKey(Integer positionGroupId);

    int insert(PositionGroup record);

    int insertSelective(PositionGroup record);

    PositionGroup selectByPrimaryKey(Integer positionGroupId);

    int updateByPrimaryKeySelective(PositionGroup record);

    int updateByPrimaryKey(PositionGroup record);
}
