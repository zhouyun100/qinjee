package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.EmailRecord;

public interface EmailRecordDao {
    int deleteByPrimaryKey(Integer emailRecordId);

    int insert(EmailRecord record);

    int insertSelective(EmailRecord record);

    EmailRecord selectByPrimaryKey(Integer emailRecordId);

    int updateByPrimaryKeySelective(EmailRecord record);

    int updateByPrimaryKeyWithBLOBs(EmailRecord record);

    int updateByPrimaryKey(EmailRecord record);
}
