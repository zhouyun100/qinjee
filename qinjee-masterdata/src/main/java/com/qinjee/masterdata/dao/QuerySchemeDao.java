package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.QueryScheme;

public interface QuerySchemeDao {
    int deleteByPrimaryKey(Integer querySchemeId);

    int insert(QueryScheme record);

    int insertSelective(QueryScheme record);

    QueryScheme selectByPrimaryKey(Integer querySchemeId);

    int updateByPrimaryKeySelective(QueryScheme record);

    int updateByPrimaryKey(QueryScheme record);
}