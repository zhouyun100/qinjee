package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.PostLevelRelation;

public interface PostLevelRelationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PostLevelRelation record);

    int insertSelective(PostLevelRelation record);

    PostLevelRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostLevelRelation record);

    int updateByPrimaryKey(PostLevelRelation record);
}