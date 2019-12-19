package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.AttachmentRecord;
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

    List<String> selectFilePath(@Param("groupName") String groupName,@Param("archiveId") Integer archiveId, @Param("companyId") Integer companyId);

    void deleteByIdList(@Param("list") List<Integer> list);

    List<AttachmentRecord> selectAttach(@Param("archiveId") Integer arichveId, @Param("companyId") Integer companyId);

    Integer selectGroupId(@Param("groupName") String groupName);

    Integer selectFileSize(@Param("groupName") String groupName);

    void deleteFile(@Param("id") Integer id, @Param("companyId") Integer companyId);

    List< String> selectGroup();
}
