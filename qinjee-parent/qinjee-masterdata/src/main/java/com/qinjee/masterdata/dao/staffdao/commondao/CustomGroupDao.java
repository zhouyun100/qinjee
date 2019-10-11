package com.qinjee.masterdata.dao.staffdao.commondao;

import com.qinjee.masterdata.model.entity.CustomGroup;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomGroupDao {
    int deleteByPrimaryKey(Integer groupId);

    int insert(CustomGroup record);

    int insertSelective(CustomGroup record);

    CustomGroup selectByPrimaryKey(Integer groupId);

    int updateByPrimaryKeySelective(CustomGroup record);

    int updateByPrimaryKey(CustomGroup record);

    Integer selectMaxPrimaryKey();

    void deleteCustomGroup(@Param("integer") Integer integer);

    List<Integer> selectTableId(@Param("customGroupId") Integer customGroupId);
}
