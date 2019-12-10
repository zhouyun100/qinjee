package com.qinjee.masterdata.dao.organation;

import com.qinjee.masterdata.model.entity.PositionLevelRelation;

public interface PositionLevelRelationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PositionLevelRelation record);

    int insertSelective(PositionLevelRelation record);

    PositionLevelRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PositionLevelRelation record);

    int updateByPrimaryKey(PositionLevelRelation record);

    /**
     * 修改是否删除
     * @param positionLevelRelation
     * @return
     */
    int updateIsDeleteByPositionLevelRelation(PositionLevelRelation positionLevelRelation);
}
