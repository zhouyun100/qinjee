package com.qinjee.masterdata.dao.staffdao.commondao;

import com.qinjee.masterdata.model.entity.CustomArchiveTableData;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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

    List<CustomArchiveTableData> selectBigDataBybusinessIdAndTableId(@Param("businessId") Integer businessId, @Param("tableId") Integer tableId);
    @MapKey ( "business_id" )
    Map< Integer, Map<String,String>>selectBigDataByBusinessIdAndTitleListAndCompanyId(@Param("list") List< Integer> list, @Param("title") String title, @Param("companyId") Integer companyId);


    void deleteByBusinessIdAndFuncode(@Param("CompanyId") Integer CompanyId, @Param("id") Integer id, @Param("funcCode") String funcCode);

    void deleteByPrimaryKeyList(@Param("list") List< Integer> list);

    void deleteByBusinessIdAndTableId(@Param("businessId") Integer businessId, @Param("tableId") Integer tableId);

    void deleteById(@Param("id") Integer id);

    List< CustomArchiveTableData> selectByBusinessIdAndCompanyId(@Param("employmentId") Integer employmentId, @Param("companyId") Integer companyId);

    int insertBatch(@Param("list5") List<CustomArchiveTableData> list5);

    List<CustomArchiveTableData> selectByCompanyIdAndBusinessId(@Param("companyId") Integer companyId, @Param("employmentId") Integer employmentId);

    void deleteByBussinessIdAndTableId(@Param("businessId") Integer businessId, @Param("integers") List<Integer> integers);
}
