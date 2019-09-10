package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.UserCompany;

public interface UserCompanyDao {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCompany record);

    int insertSelective(UserCompany record);

    UserCompany selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCompany record);

    int updateByPrimaryKey(UserCompany record);
}
