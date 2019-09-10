package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.Post;

public interface PostDao {
    int deleteByPrimaryKey(Integer postId);

    int insert(Post record);

    int insertSelective(Post record);

    Post selectByPrimaryKey(Integer postId);

    int updateByPrimaryKeySelective(Post record);

    int updateByPrimaryKey(Post record);
}