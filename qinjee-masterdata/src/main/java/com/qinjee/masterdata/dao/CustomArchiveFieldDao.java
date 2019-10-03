package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.CustomArchiveField;
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

    void deleteCustomField(Integer integer);

    List<Integer> selectFieldId(Integer customArchiveTableId);

    List<String> selectFieldType(Integer customArchiveTableId);
}
