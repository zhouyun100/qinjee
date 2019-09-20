package com.qinjee.masterdata.dao.staffdao.userarchivedao;

import com.qinjee.masterdata.model.entity.UserArchive;
import org.springframework.stereotype.Repository;

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

}
