package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.TPostInstructions;

public interface TPostInstructionsDao {
    int deleteByPrimaryKey(Integer instructionId);

    int insert(TPostInstructions record);

    int insertSelective(TPostInstructions record);

    TPostInstructions selectByPrimaryKey(Integer instructionId);

    int updateByPrimaryKeySelective(TPostInstructions record);

    int updateByPrimaryKeyWithBLOBs(TPostInstructions record);

    int updateByPrimaryKey(TPostInstructions record);
}