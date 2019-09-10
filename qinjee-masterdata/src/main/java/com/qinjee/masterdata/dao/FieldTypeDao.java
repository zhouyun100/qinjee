package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.FieldType;

public interface FieldTypeDao {
    int deleteByPrimaryKey(String typeCode);

    int insert(FieldType record);

    int insertSelective(FieldType record);

    FieldType selectByPrimaryKey(String typeCode);

    int updateByPrimaryKeySelective(FieldType record);

    int updateByPrimaryKey(FieldType record);
}
