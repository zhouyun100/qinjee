package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.CompanyCode;

public interface CompanyCodeDao {
    int deleteByPrimaryKey(Integer codeId);

    int insert(CompanyCode record);

    int insertSelective(CompanyCode record);

    CompanyCode selectByPrimaryKey(Integer codeId);

    int updateByPrimaryKeySelective(CompanyCode record);

    int updateByPrimaryKey(CompanyCode record);
}