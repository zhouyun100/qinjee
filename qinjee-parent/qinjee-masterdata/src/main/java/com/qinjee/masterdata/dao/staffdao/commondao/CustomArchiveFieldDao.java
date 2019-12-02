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


    /**
     *
     * @param list
     * @return
     */
    Integer deleteCustomField(@Param("list") List<Integer> list);



    /**
     * customArchiveFieldId
     * @param customArchiveFieldId
     * @return
     */
    Integer selectCodeId(@Param("customArchiveFieldId") Integer customArchiveFieldId);




    /**
     * fieldId
     * @param fieldId
     * @return
     */
    String selectPhysicName(@Param("fieldId") Integer fieldId);




    /**
     * selectFieldCodeByList
     * @param sortList
     * @return
     */
    List<String> selectFieldCodeByList(@Param("sortList") List<Integer> sortList);

    /**
     * updatePreEmploymentField
     * @param map
     * @return
     */
    Integer updatePreEmploymentField(@Param("map") Map<Integer, String> map);





    /**
     * selectFieldNameListByTableIdList
     * @param tableIdList
     * @return
     */
    List<String> selectFieldCodeListByTableIdList(@Param("tableIdList") List<Integer> tableIdList);

    /**
     * selectFieldNameListByTableIdList
     * @param fieldId
     * @return
     */
    String selectTypeByFieldId(@Param("fieldId") Integer fieldId);

    /**
     * customArchiveTableId
     * @param customArchiveTableId
     * @return
     */
    List<CustomArchiveField> selectFieldByTableId(@Param("customArchiveTableId") Integer customArchiveTableId);

    /**
     * companyId
     * @param strings
     * @param companyId
     * @return
     */
    List<String> selectFieldNameByCodeList(@Param("strings") List<String> strings,@Param ( "companyId" ) Integer companyId);

    /**
     * heads
     * @param heads
     * @return
     */
    List<String> selectFieldTypeByNameList(@Param("heads") List<String> heads);



    /**
     * tableId
     * @param tableId
     * @param archiveId
     * @return
     */
    List<String> selectFieldByTableIdAndAuth(@Param("tableId") Integer tableId, @Param("archiveId") Integer archiveId);

    /**
     * achiveId
     * @param achiveId
     * @param companyId
     * @return
     */
    List<String> selectFieldByArcAndAuth(@Param("achiveId") Integer achiveId, @Param("companyId") Integer companyId);

    /**
     * customArchiveGroupId
     * @param customArchiveGroupId
     * @return
     */
    List<CustomArchiveField> selectCustomArchiveField(@Param("customArchiveGroupId") Integer customArchiveGroupId);

    /**
     * companyId
     * @param s
     * @param funcCode
     * @param companyId
     * @return
     */
    String selectFieldCodeByNameAndFuncCodeAndCompanyId(@Param("s") String s, @Param ( "funcCode" ) String funcCode, @Param("companyId") Integer companyId);



    /**
     * companyId
     * @param companyId
     * @param preEmployment
     * @return
     */
    List< CustomArchiveField> selectFieldNameByTableName(@Param("companyId") Integer companyId, @Param("preEmployment") String preEmployment);

    /**
     * fieldNames
     * @param fieldNames
     * @param companyId
     * @param funcCode
     * @return
     */
    List< Integer> selectFieldIdByFieldNameAndCompanyId(@Param("fieldNames") List< String> fieldNames, @Param("companyId") Integer companyId,
                                                        @Param ( "funcCode" )String funcCode);

    /**
     * strings
     * @param strings
     * @return
     */
    @MapKey ( "field_name" )
    Map< String,Map< String, String>> seleleIsSysAndTableIdAndTableName(@Param("strings") List< String> strings);


    /**
     * head
     * @param head
     * @param companyId
     * @return
     */
    String selectFieldCodeByName(@Param("head") String head, @Param("companyId") Integer companyId);

    /**
     * key
     * @param key
     * @param companyId
     * @param funcCode
     * @return
     */
    Integer selectFieldIdByFieldNameAndCompanyIdAndFuncCode(@Param("key") String key, @Param("companyId") Integer companyId, @Param("funcCode") String funcCode);

    /**
     * 根据id找到一系列属性
     * @param idList
     * @return
     */
    @MapKey ( "field_id" )
    Map<Integer,Map<String,String>> selectNameAndIdAndIsSystemDefine(@Param("idList") List< Integer> idList);

     Integer selectSymbolForPreIdNumber(@Param("list") List< Integer> list);
     Integer selectSymbolForPreIdType(@Param("list") List< Integer> list);
     Integer selectSymbolForArcEmploymentNumber(@Param("list") List< Integer> list);
     Integer selectSymbolForArcIdNumber(@Param("list") List< Integer> list);

    Integer selectTableIdByFieldId(@Param("integer1") Integer integer1);



    Map< String, String> selectCodeAndTypeById(@Param("integer") Integer integer);
}
