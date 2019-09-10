package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.PostInstructions;

public interface PostInstructionsDao {
    int deleteByPrimaryKey(Integer instructionId);

    int insert(PostInstructions record);

    int insertSelective(PostInstructions record);

    PostInstructions selectByPrimaryKey(Integer instructionId);

    int updateByPrimaryKeySelective(PostInstructions record);

    int updateByPrimaryKeyWithBLOBs(PostInstructions record);

    int updateByPrimaryKey(PostInstructions record);
}
