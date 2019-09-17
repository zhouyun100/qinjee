package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.FieldCheckType;

public interface FieldCheckTypeDao {
    int deleteByPrimaryKey(Integer id);

    int insert(FieldCheckType record);

    int insertSelective(FieldCheckType record);

    FieldCheckType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FieldCheckType record);

    int updateByPrimaryKey(FieldCheckType record);
}