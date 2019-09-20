package com.qinjee.masterdata.dao;


import com.qinjee.masterdata.model.entity.PositionGrade;

import java.util.List;

public interface PositionGradeDao {
    int deleteByPrimaryKey(Integer positionGradeId);

    int insert(PositionGrade record);

    int insertSelective(PositionGrade record);

    PositionGrade selectByPrimaryKey(Integer positionGradeId);

    int updateByPrimaryKeySelective(PositionGrade record);

    int updateByPrimaryKey(PositionGrade record);

    /**
     * 根据职位id获取职等
     * @param positionId
     * @return
     */
    List<PositionGrade> getPositionGradeListByPositionId(Integer positionId);
}
