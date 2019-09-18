package com.qinjee.masterdata.dao.staffdao.commondao;

import com.qinjee.masterdata.model.entity.FieldType;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldTypeDao {
    int deleteByPrimaryKey(String typeCode);

    int insert(FieldType record);

    int insertSelective(FieldType record);

    FieldType selectByPrimaryKey(String typeCode);

    int updateByPrimaryKeySelective(FieldType record);

    int updateByPrimaryKey(FieldType record);

    String selectFieldCode(String s);
}
