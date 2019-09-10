package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.SmsConfig;

public interface SmsConfigDao {
    int deleteByPrimaryKey(Integer smsConfigId);

    int insert(SmsConfig record);

    int insertSelective(SmsConfig record);

    SmsConfig selectByPrimaryKey(Integer smsConfigId);

    int updateByPrimaryKeySelective(SmsConfig record);

    int updateByPrimaryKey(SmsConfig record);
}
