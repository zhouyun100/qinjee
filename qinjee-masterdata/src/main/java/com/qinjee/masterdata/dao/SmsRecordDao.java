package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.SmsRecord;

public interface SmsRecordDao {
    int deleteByPrimaryKey(Integer smsRecordId);

    int insert(SmsRecord record);

    int insertSelective(SmsRecord record);

    SmsRecord selectByPrimaryKey(Integer smsRecordId);

    int updateByPrimaryKeySelective(SmsRecord record);

    int updateByPrimaryKey(SmsRecord record);
}