package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.EmailConfig;

public interface EmailConfigDao {
    int deleteByPrimaryKey(Integer emailConfigId);

    int insert(EmailConfig record);

    int insertSelective(EmailConfig record);

    EmailConfig selectByPrimaryKey(Integer emailConfigId);

    int updateByPrimaryKeySelective(EmailConfig record);

    int updateByPrimaryKey(EmailConfig record);
}
