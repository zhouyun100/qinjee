package com.qinjee.masterdata.dao.sms;

import com.qinjee.masterdata.model.entity.SmsRecord;
import org.springframework.stereotype.Service;

@Service
public interface SmsRecordDao {

    /**
     * 添加短信发送记录
     * @param record
     * @return
     */
    int insertSelective(SmsRecord record);

}
