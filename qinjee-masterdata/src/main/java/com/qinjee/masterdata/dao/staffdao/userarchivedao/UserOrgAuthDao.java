package com.qinjee.masterdata.dao.staffdao.userarchivedao;

import com.qinjee.masterdata.model.entity.UserOrgAuth;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOrgAuthDao {
    int deleteByPrimaryKey(Integer authId);

    int insert(UserOrgAuth record);

    int insertSelective(UserOrgAuth record);

    UserOrgAuth selectByPrimaryKey(Integer authId);

    int updateByPrimaryKeySelective(UserOrgAuth record);

    int updateByPrimaryKey(UserOrgAuth record);

    List<Integer> selectCompanyIdByArchive(@Param("archiveId") Integer archiveId);

    List<Integer> selectArchiveIdByOrg(@Param("integer") Integer integer);

    List<Integer> selectOrgByArc(@Param("archiveId") Integer archiveId);
}
