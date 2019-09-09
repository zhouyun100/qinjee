package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.TPositionLevel;
import com.qinjee.masterdata.entity.TPositionLevelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TPositionLevelDao {
    long countByExample(TPositionLevelExample example);

    int deleteByExample(TPositionLevelExample example);

    int deleteByPrimaryKey(Integer positionLevelId);

    int insert(TPositionLevel record);

    int insertSelective(TPositionLevel record);

    List<TPositionLevel> selectByExample(TPositionLevelExample example);

    TPositionLevel selectByPrimaryKey(Integer positionLevelId);

    int updateByExampleSelective(@Param("record") TPositionLevel record, @Param("example") TPositionLevelExample example);

    int updateByExample(@Param("record") TPositionLevel record, @Param("example") TPositionLevelExample example);

    int updateByPrimaryKeySelective(TPositionLevel record);

    int updateByPrimaryKey(TPositionLevel record);
}