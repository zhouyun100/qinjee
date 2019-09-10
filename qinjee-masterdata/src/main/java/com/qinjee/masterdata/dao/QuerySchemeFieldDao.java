package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.QuerySchemeField;

public interface QuerySchemeFieldDao {
    int deleteByPrimaryKey(Integer querySchemeFieldId);

    int insert(QuerySchemeField record);

    int insertSelective(QuerySchemeField record);

    QuerySchemeField selectByPrimaryKey(Integer querySchemeFieldId);

    int updateByPrimaryKeySelective(QuerySchemeField record);

    int updateByPrimaryKey(QuerySchemeField record);
}