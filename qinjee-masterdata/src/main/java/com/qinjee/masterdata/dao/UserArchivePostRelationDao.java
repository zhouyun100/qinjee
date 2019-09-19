package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.UserArchivePostRelation;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 */
@Repository
public interface UserArchivePostRelationDao {


    int deleteByPrimaryKey(Integer id);

    int insert(UserArchivePostRelation record);

    int insertSelective(UserArchivePostRelation record);

    UserArchivePostRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserArchivePostRelation record);

    int updateByPrimaryKey(UserArchivePostRelation record);

    Integer selectMaxPrimaryKey();

    void deleteUserArchivePostRelation(Integer integer);

}
