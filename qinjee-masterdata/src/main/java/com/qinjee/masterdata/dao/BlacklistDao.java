package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.Blacklist;

public interface BlacklistDao {
    int deleteByPrimaryKey(Integer blacklistId);

    int insert(Blacklist record);

    int insertSelective(Blacklist record);

    Blacklist selectByPrimaryKey(Integer blacklistId);

    int updateByPrimaryKeySelective(Blacklist record);

    int updateByPrimaryKey(Blacklist record);
}
