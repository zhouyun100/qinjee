package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.AttachmentRecord;
import com.qinjee.masterdata.model.vo.staff.GetFilePath;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRecordDao {
    int deleteByPrimaryKey(Integer attachmentId);

    int insert(AttachmentRecord record);

    int insertSelective(AttachmentRecord record);

    AttachmentRecord selectByPrimaryKey(Integer attachmentId);

    int updateByPrimaryKeySelective(AttachmentRecord record);

    int updateByPrimaryKey(AttachmentRecord record);

    List<String> selectFilePath(@Param("getFilePath") GetFilePath getFilePath, @Param("companyId") Integer companyId);

    void deleteByIdList(@Param("list") List<Integer> list);

    List<AttachmentRecord> selectAttach(@Param("businessModule") String businessModule, @Param("businessType") String businessType, @Param("archiveId") Integer archiveId, @Param("groupName") String groupName, @Param("companyId") Integer companyId);
}
