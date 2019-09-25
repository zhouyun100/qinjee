package com.qinjee.masterdata.dao.sms;

import com.qinjee.masterdata.model.entity.SmsRecord;
import org.springframework.stereotype.Service;

/**
 * 短信记录
 * @author 周赟
 * @date 2019/9/25
 */
@Service
public interface SmsRecordDao {

    /**
     * 添加短信发送记录
     * @param record
     * @return
     */
    int insertSelective(SmsRecord record);

}
