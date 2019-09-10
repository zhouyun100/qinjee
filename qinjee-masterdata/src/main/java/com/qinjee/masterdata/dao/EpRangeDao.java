package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.EpRange;

public interface EpRangeDao {
    int deleteByPrimaryKey(Integer rangeId);

    int insert(EpRange record);

    int insertSelective(EpRange record);

    EpRange selectByPrimaryKey(Integer rangeId);

    int updateByPrimaryKeySelective(EpRange record);

    int updateByPrimaryKey(EpRange record);
}