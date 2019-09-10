package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.UserArchive;

public interface UserArchiveDao {
    int deleteByPrimaryKey(Integer archiveId);

    int insert(UserArchive record);

    int insertSelective(UserArchive record);

    UserArchive selectByPrimaryKey(Integer archiveId);

    int updateByPrimaryKeySelective(UserArchive record);

    int updateByPrimaryKey(UserArchive record);
}