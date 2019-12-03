package com.qinjee.masterdata.dao.staffdao.commondao;

import com.qinjee.masterdata.model.entity.CustomArchiveTable;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CustomArchiveTableDao {
    int deleteByPrimaryKey(Integer tableId);

    int insert(CustomArchiveTable record);

    int insertSelective(CustomArchiveTable record);

    CustomArchiveTable selectByPrimaryKey(Integer tableId);

    int updateByPrimaryKeySelective(CustomArchiveTable record);

    int updateByPrimaryKey(CustomArchiveTable record);

    Integer selectMaxPrimaryKey();


    List<CustomArchiveTable> selectByPage(Integer comId);

    Integer deleteCustomTable(List<Integer> list);

    List<CustomArchiveTable> selectByPrimaryKeyList(List<Integer> list);


    Integer selectInside(@Param("tableId") Integer tableId);

    String selectTableName(@Param("tableId") Integer tableId);

    Integer selectTableIdByNameAndCompanyId(@Param("tableName") String tableName, @Param("companyId") Integer companyId);

    List<Integer> selectidbycomidandfunccode(@Param("companyId") Integer companyId);


    String selectFuncCode(@Param("tableId") Integer tableId);

    Integer selectByComIdAndPhyName(@Param("companyId") Integer companyId, @Param("archive") String archive);

    List<Integer> selectNotInsideTableId(@Param("companyId") Integer companyId, @Param("archive") String archive);

    List<String> selectNameBycomId(@Param("companyId") Integer companyId);

    Map< String, String> selectIsSysAndFuncCode(@Param("tableId") Integer tableId);

    Map<String,String> selectFieldIdBySortList(@Param("schemeId") Integer schemeId);

    List< Integer> selectFieldIdNotInside(Integer companyId);
}
