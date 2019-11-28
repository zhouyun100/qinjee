package com.qinjee.masterdata.dao.staffdao.commondao;

import com.qinjee.masterdata.model.entity.CustomArchiveTableData;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CustomArchiveTableDataDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomArchiveTableData record);

    int insertSelective(CustomArchiveTableData record);

    CustomArchiveTableData selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomArchiveTableData record);

    int updateByPrimaryKeyWithBLOBs(CustomArchiveTableData record);

    int updateByPrimaryKey(CustomArchiveTableData record);

    List<Integer> selectCustomArchiveTableId(Integer customArchiveTableId);

    List<CustomArchiveTableData> selectByPrimaryKeyList(@Param("integerList") List<Integer> integerList);

    List<Integer> selectTableIdByBusinessId(Integer id);

    List<CustomArchiveTableData> selectByTableId(@Param("tableId") Integer tableId);

    void insertCustom(String s, String fieldSql, String valueSql);

    Integer selectTableIdByBusinessIdAndTableId(Integer businessId, Integer tableId);

    String selectBigDataBybusinessIdAndTableId(@Param("id") Integer id, @Param("integer") Integer integer);
    @MapKey ( "business_id" )
    Map< Integer, Map<String,String>>selectBigDataByBusinessIdAndTitleListAndCompanyId(@Param("list") List< Integer> list, @Param("title") String title, @Param("companyId") Integer companyId);
}
