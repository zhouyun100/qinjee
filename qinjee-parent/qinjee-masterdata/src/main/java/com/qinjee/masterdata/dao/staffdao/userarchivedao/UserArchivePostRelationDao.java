package com.qinjee.masterdata.dao.staffdao.userarchivedao;

import com.qinjee.masterdata.model.entity.UserArchivePostRelation;
import com.qinjee.masterdata.model.vo.staff.UserArchivePostRelationVo;
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


    void deleteUserArchivePostRelation(@Param("list") List<Integer> list);

    /**
     * 通过岗位id查询员工档案岗位关系表
     * @param postId
     * @return
     */
    List<UserArchivePostRelation> getUserArchivePostRelationList(@Param("postId") Integer postId);

    List< UserArchivePostRelationVo > selectByPrimaryKeyList(@Param("archiveId") Integer archiveId);

    UserArchivePostRelation selectByArcId(@Param("id") Integer id);

    String selectEmploymentTypeByArichiveId(@Param("archiveId") Integer archiveId);
}
