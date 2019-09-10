package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.CustomArchiveTable;

public interface CustomArchiveTableDao {
    int deleteByPrimaryKey(Integer tableId);

    int insert(CustomArchiveTable record);

    int insertSelective(CustomArchiveTable record);

    CustomArchiveTable selectByPrimaryKey(Integer tableId);

    int updateByPrimaryKeySelective(CustomArchiveTable record);

    int updateByPrimaryKey(CustomArchiveTable record);
}
