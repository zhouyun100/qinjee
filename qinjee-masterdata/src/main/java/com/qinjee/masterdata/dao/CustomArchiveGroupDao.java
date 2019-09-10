package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.CustomArchiveGroup;

public interface CustomArchiveGroupDao {
    int deleteByPrimaryKey(Integer groupId);

    int insert(CustomArchiveGroup record);

    int insertSelective(CustomArchiveGroup record);

    CustomArchiveGroup selectByPrimaryKey(Integer groupId);

    int updateByPrimaryKeySelective(CustomArchiveGroup record);

    int updateByPrimaryKey(CustomArchiveGroup record);
}
