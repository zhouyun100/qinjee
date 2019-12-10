package com.qinjee.masterdata.dao.organation;

import com.qinjee.masterdata.model.entity.PostGradeRelation;

import java.util.List;

public interface PostGradeRelationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PostGradeRelation record);

    int insertSelective(PostGradeRelation record);

    PostGradeRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostGradeRelation record);

    int updateByPrimaryKey(PostGradeRelation record);

    /**
     * 通过岗位id查询岗位职等关系信息
     * @param postId
     * @return
     */
    List<PostGradeRelation> getPostGradeRelationByPostId(Integer postId);
}
