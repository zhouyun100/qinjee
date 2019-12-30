package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoDao {
    int deleteByPrimaryKey(Integer userId);

    int insert(UserInfo record);


    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    Integer selectUserIdByPhone(@Param("phone") String phone);
}
