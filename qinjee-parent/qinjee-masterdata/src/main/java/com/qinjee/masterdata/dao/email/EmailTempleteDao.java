package com.qinjee.masterdata.dao.email;

import com.qinjee.masterdata.model.entity.EmailTemplete;

public interface EmailTempleteDao {
    int deleteByPrimaryKey(Integer emailTempleteId);

    int insert(EmailTemplete record);

    int insertSelective(EmailTemplete record);

    EmailTemplete selectByPrimaryKey(Integer emailTempleteId);

    int updateByPrimaryKeySelective(EmailTemplete record);

    int updateByPrimaryKey(EmailTemplete record);
}
