package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.RoleDataLevelAuth;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDataLevelAuthDao {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleDataLevelAuth record);

    int insertSelective(RoleDataLevelAuth record);

    RoleDataLevelAuth selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleDataLevelAuth record);

    int updateByPrimaryKey(RoleDataLevelAuth record);

    RoleDataLevelAuth selectArchive(Integer archiveid);
}
