package com.qinjee.masterdata.dao.email;

import com.qinjee.masterdata.model.entity.EmailRecord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface EmailRecordDao {
    EmailRecord selectAll();

    EmailRecord selectEmailRecordByCondition(Map<String, Object> map);

    List<EmailRecord>  findEmailRecordByIds(Map<String, Object> map);

    void insertEmailRecord(EmailRecord emailRecord);

    void insertEmailRecords(List<EmailRecord> list);



}
