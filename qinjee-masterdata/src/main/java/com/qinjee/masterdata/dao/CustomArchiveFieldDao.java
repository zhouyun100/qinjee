package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.CustomArchiveField;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    Integer selectTableId(@Param("fieldId") Integer fieldId);
}
