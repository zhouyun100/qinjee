package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.PositionGroup;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedList;
import java.util.List;

public interface PositionGroupDao {
    int deleteByPrimaryKey(Integer positionGroupId);

    int insert(PositionGroup record);

    int insertSelective(PositionGroup record);

    PositionGroup selectByPrimaryKey(Integer positionGroupId);

    List<PositionGroup> selectBypositionGroupIds(@Param("positionGroupIds")List<Integer> positionGroupIds);

    int updateByPrimaryKeySelective(PositionGroup record);

    int updateByPrimaryKey(PositionGroup record);


    /**
     * 获取职位族
     * @param positionGroup
     * @return
     */
    List<PositionGroup> getPositionGroupByPosG(PositionGroup positionGroup);

    /**
     * 批量逻辑删除删除
     * @param positionGroupIds
     * @return
     */
    int batchDeleteByGroupIds(@Param("positionGroupIds") List<Integer> positionGroupIds);

    void sortPositionGroup(@Param("positionGroupIds")LinkedList<String> positionGroupIds);

}
