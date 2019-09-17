package com.qinjee.masterdata.dao.sms;

import com.qinjee.masterdata.model.entity.SmsConfig;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SmsConfigDao {

    /**
     * 根据业务类型查询对应业务有效的短信配置信息
     * @param businessType
     * @return
     */
    List<SmsConfig> selectByBusinessType(String businessType);

}
