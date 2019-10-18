package com.qinjee.masterdata.dao.staffdao.commondao;

import com.qinjee.masterdata.model.entity.CustomArchiveField;
import com.qinjee.masterdata.model.entity.CustomField;
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

    List<String> selectPhysicNameByList(List<String> strings);

    List<String> selectTypeByNameList(List<String> heads);

    List<String> selectFieldNameByList(@Param("list") List<Integer> list);

    void updatePreEmploymentField(Map<Integer, String> map);

    List<CustomField> selectFieldByTableiId(@Param("id") Integer id);

    Short isSystemField(@Param("head") String head);

    Integer selectTableIdByFieldName(@Param("head") String head);

    Short selectIsInsideByName(@Param("filedName") String filedName);

    List<String> selectFieldNameListByTableIdList(@Param("tableIdList") List<Integer> tableIdList);

    String selectTypeByFieldId(@Param("fieldId") Integer fieldId);
}
