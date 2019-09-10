package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.TPositionGroup;

public interface TPositionGroupDao {
    int deleteByPrimaryKey(Integer positionGroupId);

    int insert(TPositionGroup record);

    int insertSelective(TPositionGroup record);

    TPositionGroup selectByPrimaryKey(Integer positionGroupId);

    int updateByPrimaryKeySelective(TPositionGroup record);

    int updateByPrimaryKey(TPositionGroup record);
}
