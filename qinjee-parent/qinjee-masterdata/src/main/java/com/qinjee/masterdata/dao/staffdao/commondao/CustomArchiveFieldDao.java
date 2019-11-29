package com.qinjee.masterdata.dao.staffdao.commondao;

import com.qinjee.masterdata.model.entity.CustomArchiveField;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CustomArchiveFieldDao {
    int deleteByPrimaryKey(Integer fieldId);

    int insert(CustomArchiveField record);

    int insertSelective(CustomArchiveField record);

    CustomArchiveField selectByPrimaryKey(Integer fieldId);

    int updateByPrimaryKeySelective(CustomArchiveField record);

    int updateByPrimaryKey(CustomArchiveField record);

    Integer selectMaxPrimaryKey();

    List<Integer> selectFieldId(@Param("customArchiveTableId") Integer customArchiveTableId);

    List<String> selectFieldType(@Param("customArchiveTableId") Integer customArchiveTableId);

    Integer deleteCustomField(@Param("list") List<Integer> list);

    List<CustomArchiveField> selectByList(@Param("integerList") List<Integer> integerList);

    List<CustomArchiveField> selectByPrimaryKeyList(@Param("integerList") List<Integer> list);

    Integer selectCodeId(@Param("customArchiveFieldId") Integer customArchiveFieldId);

    String selectFieldName(@Param("fieldId") Integer fieldId);

    Integer selectTableId(@Param("fieldId") Integer fieldId);

    String selectPhysicName(@Param("fieldId") Integer fieldId);

    List<String> selectFieldNameListByTableId(@Param("id") Integer id);

    List<String> selectTypeByNameList(@Param("heads") List<String> heads);

    List<String> selectFieldCodeByList(@Param("sortList") List<Integer> sortList);

    Integer updatePreEmploymentField(@Param("map") Map<Integer, String> map);

    Short isSystemField(@Param("head") String head);

    Integer selectTableIdByFieldName(@Param("head") String head);

    Short selectIsInsideByName(@Param("filedName") String filedName);

    List<String> selectFieldNameListByTableIdList(@Param("tableIdList") List<Integer> tableIdList);

    String selectTypeByFieldId(@Param("fieldId") Integer fieldId);

    List<CustomArchiveField> selectFieldByTableId(@Param("customArchiveTableId") Integer customArchiveTableId);

    List<String> selectFieldNameByCodeList(@Param("strings") List<String> strings);

    List<String> selectFieldTypeByNameList(@Param("heads") List<String> heads);

    List<String> selectFieldNameByList(@Param("strings") List<String> stringList);

    List<String> selectFieldNameByIntList(@Param("sortList") List<Integer> sortList);

    List<String> selectFieldByTableIdAndAuth(@Param("tableId") Integer tableId, @Param("archiveId") Integer archiveId);

    List<String> selectFieldByArcAndAuth(@Param("achiveId") Integer achiveId, @Param("companyId") Integer companyId);

    List<CustomArchiveField> selectCustomArchiveField(@Param("customArchiveGroupId") Integer customArchiveGroupId);

    String selectFieldCodeByName(@Param("s") String s);

    List<String> selectFieldCodeByNameList(@Param("list") List<String> keySet);

    List< CustomArchiveField> selectFieldNameByTableName(@Param("companyId") Integer companyId, @Param("preEmployment") String preEmployment);

    List< Integer> selectFieldIdByFieldNameAndCompanyId(@Param("fieldNames") List< String> fieldNames, @Param("companyId") Integer companyId);
    @MapKey ( "field_name" )
    Map< String,Map< String, String>> seleleIsSysAndTableIdAndTableName(@Param("strings") List< String> strings);

    Integer selectTableIdByNameAndCompanyId(@Param("title") String title, @Param("companyId") Integer companyId);
}
