package com.qinjee.masterdata.dao.sms;

import com.qinjee.masterdata.model.entity.SmsConfig;

import java.util.List;

public interface SmsConfigDao {
    int deleteByPrimaryKey(Integer smsConfigId);

    int insert(SmsConfig record);

    int insertSelective(SmsConfig record);

    SmsConfig selectByPrimaryKey(Integer smsConfigId);

    int updateByPrimaryKeySelective(SmsConfig record);

    int updateByPrimaryKey(SmsConfig record);

    /**
     * 根据业务类型查询对应业务有效的短信配置信息
     * @param businessType
     * @return
     */
    List<SmsConfig> selectByBusinessType(String businessType);
}
