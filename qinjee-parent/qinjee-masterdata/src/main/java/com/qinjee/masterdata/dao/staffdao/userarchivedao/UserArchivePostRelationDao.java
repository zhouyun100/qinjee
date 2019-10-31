package com.qinjee.masterdata.dao.staffdao.userarchivedao;

import com.qinjee.masterdata.model.entity.UserArchivePostRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    void deleteUserArchivePostRelation(List<Integer> list);

    /**
     * 通过岗位id查询员工档案岗位关系表
     * @param postId
     * @return
     */
    List<UserArchivePostRelation> getUserArchivePostRelationList(@Param("postId") Integer postId);

    List<UserArchivePostRelation> selectByPrimaryKeyList(@Param("list") List<Integer> list);

    UserArchivePostRelation selectByArcId(@Param("id") Integer id);

    List<Integer> selectByType(@Param("type") String type, @Param("oneList") List<Integer> oneList);

    String selectEmploymentTypeByArichiveId(@Param("archiveId") Integer archiveId);
}
