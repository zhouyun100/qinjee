package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.CustomArchiveTable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomArchiveTableDao {
    int deleteByPrimaryKey(Integer tableId);

    int insert(CustomArchiveTable record);

    int insertSelective(CustomArchiveTable record);

    CustomArchiveTable selectByPrimaryKey(Integer tableId);

    int updateByPrimaryKeySelective(CustomArchiveTable record);

    int updateByPrimaryKey(CustomArchiveTable record);

    Integer selectMaxPrimaryKey();


    List<CustomArchiveTable> selectByPage();

    Integer deleteCustomTable(List<Integer> list);

    List<CustomArchiveTable> selectByPrimaryKeyList(List<Integer> list);
}
