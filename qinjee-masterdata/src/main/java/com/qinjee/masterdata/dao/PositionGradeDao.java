package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.PositionGrade;

public interface PositionGradeDao {
    int deleteByPrimaryKey(Integer positionGradeId);

    int insert(PositionGrade record);

    int insertSelective(PositionGrade record);

    PositionGrade selectByPrimaryKey(Integer positionGradeId);

    int updateByPrimaryKeySelective(PositionGrade record);

    int updateByPrimaryKey(PositionGrade record);
}