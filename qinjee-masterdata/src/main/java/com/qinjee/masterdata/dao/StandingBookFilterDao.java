package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.StandingBookFilter;

public interface StandingBookFilterDao {
    int deleteByPrimaryKey(Integer filterId);

    int insert(StandingBookFilter record);

    int insertSelective(StandingBookFilter record);

    StandingBookFilter selectByPrimaryKey(Integer filterId);

    int updateByPrimaryKeySelective(StandingBookFilter record);

    int updateByPrimaryKey(StandingBookFilter record);
}