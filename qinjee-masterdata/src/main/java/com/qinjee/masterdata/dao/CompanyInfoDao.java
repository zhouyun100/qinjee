package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.CompanyInfo;

public interface CompanyInfoDao {
    int deleteByPrimaryKey(Integer companyId);

    int insert(CompanyInfo record);

    int insertSelective(CompanyInfo record);

    CompanyInfo selectByPrimaryKey(Integer companyId);

    int updateByPrimaryKeySelective(CompanyInfo record);

    int updateByPrimaryKey(CompanyInfo record);
}
