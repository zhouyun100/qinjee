package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.StandingBook;

public interface StandingBookDao {
    int deleteByPrimaryKey(Integer standingBookId);

    int insert(StandingBook record);

    int insertSelective(StandingBook record);

    StandingBook selectByPrimaryKey(Integer standingBookId);

    int updateByPrimaryKeySelective(StandingBook record);

    int updateByPrimaryKey(StandingBook record);
}
