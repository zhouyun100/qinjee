package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.PostGradeRelation;

public interface PostGradeRelationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PostGradeRelation record);

    int insertSelective(PostGradeRelation record);

    PostGradeRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostGradeRelation record);

    int updateByPrimaryKey(PostGradeRelation record);
}