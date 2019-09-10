package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.CustomField;

public interface CustomFieldDao {
    int deleteByPrimaryKey(Integer fieldId);

    int insert(CustomField record);

    int insertSelective(CustomField record);

    CustomField selectByPrimaryKey(Integer fieldId);

    int updateByPrimaryKeySelective(CustomField record);

    int updateByPrimaryKey(CustomField record);
}
