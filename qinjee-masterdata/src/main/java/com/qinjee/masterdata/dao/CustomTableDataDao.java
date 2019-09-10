package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.CustomTableData;

public interface CustomTableDataDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomTableData record);

    int insertSelective(CustomTableData record);

    CustomTableData selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomTableData record);

    int updateByPrimaryKey(CustomTableData record);
}
