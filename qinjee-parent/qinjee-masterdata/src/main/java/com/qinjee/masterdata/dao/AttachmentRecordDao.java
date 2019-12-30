package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.AttachmentRecord;
import com.qinjee.masterdata.model.vo.AttchmentRecordVo;
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

    List< AttchmentRecordVo > selectAttach(@Param("orgIdList") List<Integer> orgIdList, @Param("companyId") Integer companyId);

    Integer selectGroupId(@Param("groupName") String groupName);

    Integer selectFileSize(@Param("groupName") String groupName);

    void deleteFile(@Param("list") List<Integer> list, @Param("companyId") Integer companyId);

    List< String> selectGroup();

    List< AttachmentRecord> selectByList(@Param("list") List< Integer> id);

    List< AttchmentRecordVo> selectByPreIdAndCompanyId(@Param("companyId") Integer companyId, @Param("preId") Integer preId, @Param("businessType") String businessType);

    void updateFileName(@Param("name") String name, @Param("attahmentId") Integer attahmentId);

    List < AttchmentRecordVo > selectByBusinessIdAndGroupNameAndBusinessType(@Param("businessId") Integer businessId, @Param("groupName") String groupName,  @Param("companyId") Integer companyId);

    void moveFile(@Param("attachmentId") Integer attachmentId, @Param("groupId") Integer groupId, @Param("companyId") Integer companyId);

    List< AttchmentRecordVo> selectFileFromPackage(@Param("businessId") Integer businessId, @Param("groupName") String groupName, @Param("companyId") Integer companyId);
}
