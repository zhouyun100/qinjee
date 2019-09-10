package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.EmailTemplete;

public interface EmailTempleteDao {
    int deleteByPrimaryKey(Integer emailTempleteId);

    int insert(EmailTemplete record);

    int insertSelective(EmailTemplete record);

    EmailTemplete selectByPrimaryKey(Integer emailTempleteId);

    int updateByPrimaryKeySelective(EmailTemplete record);

    int updateByPrimaryKey(EmailTemplete record);
}