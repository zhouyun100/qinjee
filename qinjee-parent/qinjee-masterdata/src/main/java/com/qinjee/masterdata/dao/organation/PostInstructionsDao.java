package com.qinjee.masterdata.dao.organation;

import com.qinjee.masterdata.model.entity.PostInstructions;

public interface PostInstructionsDao {
    int deleteByPrimaryKey(Integer instructionId);

    int insert(PostInstructions record);

    int insertSelective(PostInstructions record);

    PostInstructions selectByPrimaryKey(Integer instructionId);

    int updateByPrimaryKeySelective(PostInstructions record);

    int updateByPrimaryKeyWithBLOBs(PostInstructions record);

    int updateByPrimaryKey(PostInstructions record);

    /**
     * 根据岗位id查询岗位说明书
     * @param postId
     * @return
     */
    PostInstructions getPostInstructionsByPostId(Integer postId);
}
