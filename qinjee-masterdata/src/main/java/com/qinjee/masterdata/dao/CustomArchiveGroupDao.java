package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.CustomArchiveGroup;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomArchiveGroupDao {
    int deleteByPrimaryKey(Integer groupId);

    int insert(CustomArchiveGroup record);

    int insertSelective(CustomArchiveGroup record);

    CustomArchiveGroup selectByPrimaryKey(Integer groupId);

    int updateByPrimaryKeySelective(CustomArchiveGroup record);

    int updateByPrimaryKey(CustomArchiveGroup record);

    Integer selectMaxPrimaryKey();

    void deleteCustomGroup(@Param("integer") Integer integer);

    List<Integer> selectTableId(@Param("customArchiveGroupId") Integer customArchiveGroupId);
}
