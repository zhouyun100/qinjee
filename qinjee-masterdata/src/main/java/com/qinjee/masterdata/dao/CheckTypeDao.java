package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.CheckType;

public interface CheckTypeDao {
    int deleteByPrimaryKey(String checkCode);

    int insert(CheckType record);

    int insertSelective(CheckType record);

    CheckType selectByPrimaryKey(String checkCode);

    int updateByPrimaryKeySelective(CheckType record);

    int updateByPrimaryKey(CheckType record);
}
