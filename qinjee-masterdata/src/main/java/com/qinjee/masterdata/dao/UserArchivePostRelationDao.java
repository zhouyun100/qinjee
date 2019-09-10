package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.UserArchivePostRelation;

public interface UserArchivePostRelationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(UserArchivePostRelation record);

    int insertSelective(UserArchivePostRelation record);

    UserArchivePostRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserArchivePostRelation record);

    int updateByPrimaryKey(UserArchivePostRelation record);
}