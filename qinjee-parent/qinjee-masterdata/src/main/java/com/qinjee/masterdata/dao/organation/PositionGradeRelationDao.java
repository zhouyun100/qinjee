package com.qinjee.masterdata.dao.organation;

import com.qinjee.masterdata.model.entity.PositionGradeRelation;

public interface PositionGradeRelationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PositionGradeRelation record);

    int insertSelective(PositionGradeRelation record);

    PositionGradeRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PositionGradeRelation record);

    int updateByPrimaryKey(PositionGradeRelation record);

    /**
     * 修改是否删除
     * @param positionGradeRelation
     * @return
     */
    int updateIsDeleteByPositionGradeRelation(PositionGradeRelation positionGradeRelation);
}
