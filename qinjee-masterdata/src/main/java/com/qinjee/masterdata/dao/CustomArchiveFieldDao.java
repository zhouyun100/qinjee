package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.CustomArchiveField;

public interface CustomArchiveFieldDao {
    int deleteByPrimaryKey(Integer fieldId);

    int insert(CustomArchiveField record);

    int insertSelective(CustomArchiveField record);

    CustomArchiveField selectByPrimaryKey(Integer fieldId);

    int updateByPrimaryKeySelective(CustomArchiveField record);

    int updateByPrimaryKey(CustomArchiveField record);
}