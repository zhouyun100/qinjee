package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.TPositionGradeRelation;

public interface TPositionGradeRelationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TPositionGradeRelation record);

    int insertSelective(TPositionGradeRelation record);

    TPositionGradeRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TPositionGradeRelation record);

    int updateByPrimaryKey(TPositionGradeRelation record);
}