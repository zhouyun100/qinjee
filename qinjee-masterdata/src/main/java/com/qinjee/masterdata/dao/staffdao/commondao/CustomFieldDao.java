package com.qinjee.masterdata.dao.staffdao.commondao;

import com.qinjee.masterdata.model.entity.CustomField;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomFieldDao {
    int deleteByPrimaryKey(Integer fieldId);

    int insert(CustomField record);

    int insertSelective(CustomField record);

    CustomField selectByPrimaryKey(Integer fieldId);

    int updateByPrimaryKeySelective(CustomField record);

    int updateByPrimaryKey(CustomField record);

    Integer selectMaxPrimaryKey();

    void deleteCustomField(Integer integer);

    List<Integer> selectFieldId(Integer customTableId);

    List<String> selectFieldType(Integer customTableId);
}
