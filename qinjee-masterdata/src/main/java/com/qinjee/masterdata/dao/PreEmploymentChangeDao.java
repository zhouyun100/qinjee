package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.PreEmploymentChange;

public interface PreEmploymentChangeDao {
    int deleteByPrimaryKey(Integer changeId);

    int insert(PreEmploymentChange record);

    int insertSelective(PreEmploymentChange record);

    PreEmploymentChange selectByPrimaryKey(Integer changeId);

    int updateByPrimaryKeySelective(PreEmploymentChange record);

    int updateByPrimaryKey(PreEmploymentChange record);
}