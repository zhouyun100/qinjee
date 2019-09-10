package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.CustomArchiveFieldCheck;

public interface CustomArchiveFieldCheckDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomArchiveFieldCheck record);

    int insertSelective(CustomArchiveFieldCheck record);

    CustomArchiveFieldCheck selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomArchiveFieldCheck record);

    int updateByPrimaryKey(CustomArchiveFieldCheck record);
}