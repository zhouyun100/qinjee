/**
 * 文件名：CustomTableFieldDao
 * 工程名称：masterdata-v1.0-20191021
 * <p>
 * qinjee
 * 创建日期：2019/11/28
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.dao.custom;

import com.qinjee.masterdata.model.entity.CustomArchiveField;
import com.qinjee.masterdata.model.vo.custom.CustomFieldVO;
import com.qinjee.masterdata.model.vo.custom.CustomGroupVO;
import com.qinjee.masterdata.model.vo.custom.CustomTableVO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author 周赟
 * @date 2019/11/28
 */
@Repository
public interface CustomTableFieldDao {

    /**
     * 根据字段ID列表查询各字段详细属性信息
     *
     * @param fileIdList
     * @return
     */
    List < CustomFieldVO > searchCustomFieldListByFieldIdList(List < Integer > fileIdList);

    /**
     * 查询对应业务模块的自定义表
     *
     * @param customTableVO
     * @return
     */
    LinkedList < CustomTableVO > searchCustomTableListByCompanyIdAndFuncCode(CustomTableVO customTableVO);

    /**
     * 查询表对应的组信息列表
     *
     * @param companyId 企业ID
     * @param tableCode 表CODE
     * @param tableId   表ID
     * @return
     */
    List < CustomGroupVO > searchCustomGroupList(Integer companyId, String tableCode, Integer tableId);

    /**
     * 查询字段信息列表
     *
     * @param companyId 企业ID
     * @param tableCode 表CODE
     * @param archiveId 人员id
     * @param tableId   表ID
     * @return
     */
    List < CustomFieldVO > searchCustomFieldList(@Param("companyId") Integer companyId, @Param("archiveId") Integer archiveId, @Param("tableCode") String tableCode, @Param("tableId") Integer tableId);

    /**
     * 根据企业ID和功能CODE查询字段集合
     *
     * @param companyId
     * @param funcCode
     * @return
     */
    List < CustomFieldVO > searchCustomFieldListByCompanyIdAndFuncCode(@Param("companyId") Integer companyId, @Param("funcCode") String funcCode);


    /**
     * 批量逻辑删除自定义字段
     *
     * @param list
     * @return
     */
    Integer deleteCustomField(@Param("list") List < Integer > list);


    /**
     * 逻辑删除自定义字段
     * customArchiveFieldId
     *
     * @param customArchiveFieldId
     * @return
     */
    Integer selectCodeId(@Param("customArchiveFieldId") Integer customArchiveFieldId);


    /**
     * updatePreEmploymentField
     *
     * @param map
     * @return
     */
    Integer updatePreEmploymentField(@Param("map") Map < Integer, String > map);


    /**
     * companyId
     *
     * @param fieldCode
     * @param companyId
     * @return
     */
     String  selectFieldNameByCode(@Param("fieldCode") String fieldCode, @Param("companyId") Integer companyId, @Param("funcCode") String funcCode);


    /**
     * tableId
     *
     * @param tableId
     * @param archiveId
     * @return
     */
    List < String > selectFieldByTableIdAndAuth(@Param("tableId") Integer tableId, @Param("archiveId") Integer archiveId);

    /**
     * achiveId
     *
     * @param achiveId
     * @param companyId
     * @return
     */
    List < String > selectFieldByArcAndAuth(@Param("achiveId") Integer achiveId, @Param("companyId") Integer companyId);


    /**
     * companyId
     *
     * @param s
     * @param funcCode
     * @param companyId
     * @return
     */
    String selectFieldCodeByNameAndFuncCodeAndCompanyId(@Param("s") String s, @Param("funcCode") String funcCode, @Param("companyId") Integer companyId);


    /**
     * fieldNames
     *
     * @param fieldNames
     * @param companyId
     * @param funcCode
     * @return
     */
    List < Integer > selectFieldIdByFieldNameAndCompanyId(@Param("fieldNames") List < String > fieldNames, @Param("companyId") Integer companyId,
                                                          @Param("funcCode") String funcCode);

    /**
     * strings
     *
     * @param strings
     * @return
     */
    @MapKey("field_name")
    Map < String, Map < String, String > > seleleIsSysAndTableIdAndTableName(@Param("strings") List < String > strings);


    /**
     * head
     *
     * @param head
     * @param companyId
     * @return
     */
    String selectFieldCodeByName(@Param("head") String head, @Param("companyId") Integer companyId, @Param("funcCode") String funcCode);

    /**
     * key
     *
     * @param key
     * @param companyId
     * @param funcCode
     * @return
     */
    CustomFieldVO selectFieldIdByFieldNameAndCompanyIdAndFuncCode(@Param("key") String key, @Param("companyId") Integer companyId, @Param("funcCode") String funcCode);

    /**
     * 根据id找到一系列属性
     *
     * @param idList
     * @param companyId
     * @return
     */
    @MapKey("field_id")
    Map < Integer, Map < String, Integer > > selectNameAndIdAndIsSystemDefine(@Param("idList") List < Integer > idList, @Param("companyId") Integer companyId);

    /**
     * 预入职唯一标识证件号对应的fieldId
     *
     * @param list
     * @return
     */
    Integer selectSymbolForPreIdNumber(@Param("list") List < Integer > list);

    /**
     * 预入职唯一标识证件类型对应的fieldId
     *
     * @param list
     * @return
     */
    Integer selectSymbolForPreIdType(@Param("list") List < Integer > list);

    /**
     * 档案唯一标识工号类型对应的fieldId
     *
     * @param list
     * @return
     */
    Integer selectSymbolForArcEmploymentNumber(@Param("list") List < Integer > list);

    /**
     * 预入职唯一标识证件类型对应的fieldId
     *
     * @param list
     * @return
     */
    Integer selectSymbolForArcIdNumber(@Param("list") List < Integer > list);

    /**
     * 通过字段id找到tableId
     *
     * @param integer1
     * @return
     */
    Integer selectTableIdByFieldId(@Param("integer1") Integer integer1);

    /**
     * 找到字段id对应的物理code与类型
     *
     * @param integer
     * @return
     */

    Map < String, String > selectCodeAndTypeById(@Param("integer") Integer integer);

    /**
     * 根据tableId集合找到对应的物理code
     *
     * @param tableIdList
     * @return
     */
    List < String > selectFieldCodeListByTableIdList(@Param("tableIdList") List < Integer > tableIdList);


    List < CustomArchiveField > selectCustomArchiveField(Integer customArchiveGroupId);

    void insertSelective(CustomArchiveField customArchiveField);

    void updateByPrimaryKeySelective(CustomArchiveField customArchiveField);

    List < CustomArchiveField > selectFieldByTableId(Integer customArchiveTableId);

    CustomArchiveField selectByPrimaryKey(Integer customArchiveFieldId);

    List < CustomArchiveField > selectFieldNameByTableName(Integer companyId, String preEmployment);

    String selectTypeByFieldId(@Param("fieldId") Integer fieldId);

    List < Map < String, String > > selectCodAndIdByTableId(@Param("tableId") Integer tableId);

    List<Map< String, Integer>> transOrgId(@Param("funcCode") String funcCode, @Param("companyId") Integer companyId, @Param("value") String value);

    List<Map< String, Integer>> transSupiorId(@Param("funcCode") String funcCode, @Param("companyId") Integer companyId, @Param("value") String value);


    List<Map< String, Integer>> transPostId(@Param("funcCode") String funcCode, @Param("companyId") Integer companyId, @Param("value") String value);

    String selectFieldCodeById(@Param("integer") Integer integer);

    List< CustomFieldVO> selectFieldByIdList(@Param("list") List < Integer > integers2, @Param("companyId") Integer companyId, @Param("funcCode") String funcCode);

    CustomFieldVO selectFieldById(@Param("fieldId") Integer fieldId, @Param("companyId") Integer companyId, @Param("arc") String arc);


    List< Integer> selectTableIdByCompanyIdAndFuncCode(@Param("companyId") Integer companyId, @Param("funcCode") String pre);

    List< CustomFieldVO> selectFieldListByTableId(Integer tableId);

    List<Map< String, Integer>> transOrgIdByCode(@Param("funcCode") String funcCode, @Param("companyId") Integer companyId, @Param("value") String value);

    Integer selectFieldIdByCodeAndFuncCodeAndComapnyId(@Param("code") String code, @Param("funcCode") String funcCode, @Param("companyId") Integer companyId);

    Integer selectSymbolForPhone(List< Integer> isSystemDefineList);

    List< Map< String, Integer>> transSupiorIdByName(@Param("funcCode") String funcCode, @Param("companyId") Integer companyId, @Param("value") String value);

    CustomFieldVO selectFieldByCodeAndFuncCodeAndComapnyId(@Param("s") String s, @Param("arc") String arc, @Param("companyId") Integer companyId);

    CustomFieldVO selectTextTypeByFieldId(@Param("integer") Integer integer);

    String selectTextCodeByName(@Param("s") String s, @Param("companyId") Integer companyId, @Param("funcCode") String funcCode);

    Integer selectFieldIdByFieldIdAndFunccode(@Param("key") Integer key, @Param("companyId") Integer companyId);
}


