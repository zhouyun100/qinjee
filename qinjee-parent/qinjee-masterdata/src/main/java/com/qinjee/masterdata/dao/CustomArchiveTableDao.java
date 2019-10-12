package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.CustomArchiveTable;
import org.apache.ibatis.annotations.Param;
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

    List<Integer> selectIdByComId(Integer companyId);

    List<String> selectNameById(@Param("list1") List<Integer> list1);

    Integer selectInside(@Param("tableId") Integer tableId);

    String selectTableName(@Param("tableId") Integer tableId);

    Integer selectTableIdByName(@Param("tableName") String tableName);


}
