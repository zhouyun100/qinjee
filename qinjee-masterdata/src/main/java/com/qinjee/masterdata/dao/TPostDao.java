package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.TPost;

public interface TPostDao {
    int deleteByPrimaryKey(Integer postId);

    int insert(TPost record);

    int insertSelective(TPost record);

    TPost selectByPrimaryKey(Integer postId);

    int updateByPrimaryKeySelective(TPost record);

    int updateByPrimaryKey(TPost record);
}