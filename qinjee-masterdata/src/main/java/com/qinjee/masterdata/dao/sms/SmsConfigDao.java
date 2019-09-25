package com.qinjee.masterdata.dao.sms;

import com.qinjee.masterdata.model.entity.SmsConfig;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 短信配置
 * @author 周赟
 * @date 2019/9/25
 */
@Service
public interface SmsConfigDao {

    /**
     * 根据业务类型查询对应业务有效的短信配置信息
     * @param businessType
     * @return
     */
    List<SmsConfig> selectByBusinessType(String businessType);
}
