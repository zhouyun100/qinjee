package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.TPostLevelRelation;

public interface TPostLevelRelationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TPostLevelRelation record);

    int insertSelective(TPostLevelRelation record);

    TPostLevelRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TPostLevelRelation record);

    int updateByPrimaryKey(TPostLevelRelation record);
}