package com.qinjee.masterdata.dao.email;

import com.qinjee.masterdata.model.entity.EmailRecord;
import org.springframework.stereotype.Repository;

/**
 * @author 周赟
 * @date 2019/11/13
 */
@Repository
public interface EmailRecordDao {

    /**
     * 添加邮件发送记录
     * @param record
     * @return
     */
    int insertSelective(EmailRecord record);
}
