package com.qinjee.masterdata.dao.staffdao.commondao;

import com.qinjee.masterdata.model.entity.CustomField;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Repository
public interface CustomFieldDao {
    int deleteByPrimaryKey(Integer fieldId);

    int insert(CustomField record);

    int insertSelective(CustomField record);

    CustomField selectByPrimaryKey(Integer fieldId);

    int updateByPrimaryKeySelective(CustomField record);

    int updateByPrimaryKey(CustomField record);

    Integer selectMaxPrimaryKey();

    void deleteCustomField(@Param("integer") Integer integer);

    List<Integer> selectFieldId(@Param("customTableId") Integer customTableId);

    List<String> selectFieldType(@Param("customTableId") Integer customTableId);

    List<CustomField> selectPreEmploymentField(Integer id);

    String selectFieldName(@Param("integer") Integer integer);

    void updatePreEmploymentField(@Param("map") Map<Integer, String> map);
}
