package com.qinjee.masterdata.dao.email;

import com.qinjee.masterdata.model.entity.EmailRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface EmailRecordDao {
    EmailRecord selectAll();

    EmailRecord selectEmailRecordByCondition(Map<String, Object> map);

    List<EmailRecord>  findEmailRecordByIds(List<Integer> list);

    void insertEmailRecord(EmailRecord emailRecord);

    void insertBatch (@Param("list") List<EmailRecord> list);



}
