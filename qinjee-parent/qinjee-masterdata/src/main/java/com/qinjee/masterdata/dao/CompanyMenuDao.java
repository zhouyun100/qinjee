package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.CompanyMenu;

public interface CompanyMenuDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CompanyMenu record);

    int insertSelective(CompanyMenu record);

    CompanyMenu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CompanyMenu record);

    int updateByPrimaryKey(CompanyMenu record);
}
