package com.qinjee.masterdata.dao.staffdao.userarchivedao;

import com.qinjee.masterdata.model.entity.UserArchive;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 */
@Repository
public interface UserArchiveDao {
    int deleteByPrimaryKey(Integer archiveId);

    int insert(UserArchive record);

    int insertSelective(UserArchive record);

    UserArchive selectByPrimaryKey(Integer archiveId);

    int updateByPrimaryKeySelective(UserArchive record);

    int updateByPrimaryKey(UserArchive record);

    Integer selectMaxId();

    String selectMail(Integer integer);

    List<UserArchive> selectNotInList(@Param("readyIdList") List<Integer> readyIdList);

    void deleteArchiveById(@Param("archiveid") Integer archiveid);

    void resumeDeleteArchiveById(@Param("archiveid") Integer archiveid);
}
