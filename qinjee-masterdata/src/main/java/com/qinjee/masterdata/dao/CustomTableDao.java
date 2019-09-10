package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.CustomTable;

public interface CustomTableDao {
    int deleteByPrimaryKey(Integer tableId);

    int insert(CustomTable record);

    int insertSelective(CustomTable record);

    CustomTable selectByPrimaryKey(Integer tableId);

    int updateByPrimaryKeySelective(CustomTable record);

    int updateByPrimaryKey(CustomTable record);
}
