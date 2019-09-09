package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.TPositionGrade;

public interface TPositionGradeDao {
    int deleteByPrimaryKey(Integer positionGradeId);

    int insert(TPositionGrade record);

    int insertSelective(TPositionGrade record);

    TPositionGrade selectByPrimaryKey(Integer positionGradeId);

    int updateByPrimaryKeySelective(TPositionGrade record);

    int updateByPrimaryKey(TPositionGrade record);
}