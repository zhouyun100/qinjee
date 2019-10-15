package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.EpContainer;

public interface EpContainerDao {
    int deleteByPrimaryKey(Integer containerId);

    int insert(EpContainer record);

    int insertSelective(EpContainer record);

    EpContainer selectByPrimaryKey(Integer containerId);

    int updateByPrimaryKeySelective(EpContainer record);

    int updateByPrimaryKey(EpContainer record);
}
