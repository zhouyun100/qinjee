package com.qinjee.masterdata.dao.organation;

import com.qinjee.masterdata.model.entity.Post;
import com.qinjee.masterdata.model.entity.UserArchivePostRelation;
import com.qinjee.masterdata.model.vo.organization.page.PostPageVo;
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

    Post selectByPrimaryKey(Integer postId);

    int updateByPrimaryKeySelective(Post record);

    int updateByPrimaryKey(Post record);

    /**
     * 分页查询岗位列表
     *
     * @param postPageVo
     * @param sortFieldStr
     * @param archiveId
     * @return
     */
    List<Post> getPostList(@Param("postPageVo") PostPageVo postPageVo, @Param("sortFieldStr") String sortFieldStr, @Param("archiveId") Integer archiveId, @Param("now") Date now);

    /**
     * 根据机构id查询机构下的岗位
     *
     * @param orgId
     * @return
     */
    List<Post> getPostListByOrgId(@Param("orgId") Integer orgId, @Param("isEnable") Short isEnable);

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

    /**
     * 根据岗位id查询岗位
     * @param postIds
     * @return
     */
    List<Post> getPostListByPostIds(@Param("postIds")List<Integer> postIds);

    /**
     * 根据机构id查询机构下的岗位
     *
     * @param orgId
     * @return
     */
    List<Map<Integer,String>> getPostByOrgId(@Param("orgId") Integer orgId);




    List<Post> listPostByParentPostId(@Param("parentPostId") Integer parentPostId);

    List<Post> listPostByOrgIds(@Param("orgidList")List<Integer> orgidList);


    List<Post> getPostConditionPages(@Param("postPageVo") PostPageVo postPageVo,@Param("orgIdList") List<Integer> orgIdList, @Param("postIdList") List<Integer> postIdList);

    Integer sortPorts(@Param("postIds")List<Integer> postIds);

    Map< String, Integer> selectPostIdAndOrgId(@Param("org_code") String org_code, @Param("post_code") String post_code);

    Map< String, Integer> selectPostIdAndOrgIdAndsupiorId(@Param("org_code") String org_code, @Param("post_code") String post_code, @Param("code") String code);

  List<UserArchivePostRelation> getPostSuccessive(@Param("postId")Integer postId);

    List<Post> getPostGraphics( List<Integer> postIdList, Short isEnable);

    List<Post> listPostByCompanyId(Integer companyId, Short isEnable);

    List<Post> listDirectPostPage(PostPageVo postPageVo);

    Post getPostByPostCode(String postCode, Integer companyId);

    void batchDelete(@Param("idList") List<Integer> idList);

    List<Post> listPostByPisitionId(@Param("positionIds")List<Integer> positionIds);

    Post getPostById(String postId);

    List< Post> selectPostByOrgId(@Param("orgId") Integer orgId, @Param("companyId") Integer companyId);

    List<Post> getPostListByCompanyId(Integer companyId);

    List<Integer> getPostIds(Integer postId,Short isEnable);

}

