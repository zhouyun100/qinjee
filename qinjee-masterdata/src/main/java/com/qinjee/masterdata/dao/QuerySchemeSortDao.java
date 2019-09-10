package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.QuerySchemeSort;

public interface QuerySchemeSortDao {
    int deleteByPrimaryKey(Integer querySchemeSortId);

    int insert(QuerySchemeSort record);

    int insertSelective(QuerySchemeSort record);

    QuerySchemeSort selectByPrimaryKey(Integer querySchemeSortId);

    int updateByPrimaryKeySelective(QuerySchemeSort record);

    int updateByPrimaryKey(QuerySchemeSort record);
}