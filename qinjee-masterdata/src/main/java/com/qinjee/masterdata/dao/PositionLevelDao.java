package com.qinjee.masterdata.dao;


import com.qinjee.masterdata.model.entity.PositionLevel;

import java.util.List;

public interface PositionLevelDao {
    int deleteByPrimaryKey(Integer positionLevelId);

    int insert(PositionLevel record);

    int insertSelective(PositionLevel record);

    PositionLevel selectByPrimaryKey(Integer positionLevelId);

    int updateByPrimaryKeySelective(PositionLevel record);

    int updateByPrimaryKey(PositionLevel record);

    /**
     * 根据职位id获取对应的职级
     * @param positionId
     * @return
     */
    List<PositionLevel> getPositionLevelListByPositionId(Integer positionId);

    /**
     * 获取所以职级
     * @return
     */
    List<PositionLevel> getPositionLevelList();
}
