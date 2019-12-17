package com.qinjee.masterdata.dao.organation;

import com.qinjee.masterdata.model.entity.PostLevelRelation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostLevelRelationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PostLevelRelation record);

    int insertSelective(PostLevelRelation record);

    PostLevelRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostLevelRelation record);

    int updateByPrimaryKey(PostLevelRelation record);

    /**
     * 通过岗位id查询岗位职级信息
     * @param postId
     * @return
     */
    List<PostLevelRelation> getPostLevelRelationByPostId(Integer postId);
}
