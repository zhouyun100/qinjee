package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.PositionGradeRelation;

public interface PositionGradeRelationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PositionGradeRelation record);

    int insertSelective(PositionGradeRelation record);

    PositionGradeRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PositionGradeRelation record);

    int updateByPrimaryKey(PositionGradeRelation record);
}