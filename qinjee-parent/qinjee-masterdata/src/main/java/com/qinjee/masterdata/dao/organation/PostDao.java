package com.qinjee.masterdata.dao.organation;

import com.qinjee.masterdata.model.entity.PositionLevel;
import com.qinjee.masterdata.model.entity.Post;
import com.qinjee.masterdata.model.entity.UserArchivePostRelation;
import com.qinjee.masterdata.model.vo.organization.bo.PostPageBO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface PostDao {
    int deleteByPrimaryKey(Integer postId);

    int insert(Post record);

    int insertSelective(Post record);

    int updateByPrimaryKeySelective(Post record);

    int updateByPrimaryKey(Post record);

    void batchDeletePosts(@Param("idList") List<Integer> idList);

    Post getPostById(Integer postId);

    /**
     * 查询机构的顶级岗位，也就是父级岗位id为0
     *
     * @param orgId
     * @return
     */
    List<Post> getLastTopPostByOrgId(Integer orgId);

    /**
     * 查找对应的岗位
     *
     * @param postCode
     * @param postName
     * @return
     */
    Post getPostByPostCodeAndName(@Param("postCode") String postCode, @Param("postName") String postName);

    Map<String, Integer> getPostIdAndOrgId(@Param("org_code") String org_code, @Param("post_code") String post_code);

    Post getPostsByPostCode(String postCode, Integer companyId);

    Post getPostByPostId(String postId);


    //-----------------------------------------

    /**
     * 分页查询岗位列表
     *
     * @param postPageBO
     * @param sortFieldStr
     * @param archiveId
     * @return
     */
    List<Post> listPosts(@Param("postPageVo") PostPageBO postPageBO, @Param("sortFieldStr") String sortFieldStr, @Param("archiveId") Integer archiveId, @Param("now") Date now);

    List<Post> listPostsByOrgId(@Param("orgId") Integer orgId, @Param("companyId") Integer companyId);

    /**
     * 根据机构id查询机构下的岗位
     *
     * @param orgId
     * @return
     */
    List<Post> listPostsByOrgIdAndEnable(@Param("orgId") Integer orgId, @Param("isEnable") Short isEnable);


    /**
     * 根据岗位id查询岗位
     *
     * @param postIds
     * @return
     */
    List<Post> listPostsByPostIds(@Param("postIds") List<Integer> postIds,@Param("whereSql")String whereSql,@Param("orderSql")String orderSql);

    /**
     * 根据机构id查询机构下的岗位
     *
     * @param orgId
     * @return
     */
    List<Map<Integer, String>> listPostMapByOrgId(@Param("orgId") Integer orgId);


    List<Post> listPostByParentPostId(@Param("parentPostId") Integer parentPostId);

    List<Post> listPostsByOrgIds(@Param("orgidList") List<Integer> orgidList,@Param("whereSql")String whereSql,@Param("orderSql")String orderSql);


    List<Post> listPostsByCondition(@Param("postPageVo") PostPageBO postPageBO, @Param("orgIdList") List<Integer> orgIdList, @Param("postIdList") List<Integer> postIdList);

    Integer sortPorts(@Param("postIds") List<Integer> postIds);


    Map<String, Integer> getPostIdAndOrgIdAndSupiorId(@Param("org_code") String org_code, @Param("post_code") String post_code, @Param("code") String code);

    List<UserArchivePostRelation> getPostSuccessive(@Param("postId") Integer postId);

    List<Post> getPostGraphics(List<Integer> postIdList, Short isEnable);

    List<Post> listPostsByCompanyIdAndEnable(Integer companyId, Short isEnable);

    List<Post> listDirectPostPage(@Param("postPageVo") PostPageBO postPageBO);


    List<Post> listPostsByPisitionId(@Param("positionIds") List<Integer> positionIds);


    List<Post> listPostsByCompanyId(Integer companyId);

    List<Integer> getPostIds(Integer postId, Short isEnable);

    List<Post> listPostsByPositionLevelId( @Param("positionLevelIds") List<Integer> positionLevelIds);

    void batchInsertPostLevelRelation(@Param("positionLevelIds")List<Integer> positionLevelIds, @Param("operatorId")Integer operatorId, @Param("postId")Integer postId);

    int batchDeletePostLevelRelation(Integer operatorId, Integer postId);

    List<Integer> listPositionLevelId(Integer postId);

    List<PositionLevel> listPositionLevel(Integer postId);
}

