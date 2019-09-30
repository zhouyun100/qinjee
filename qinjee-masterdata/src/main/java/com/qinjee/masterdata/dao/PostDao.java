package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.model.entity.Post;
import com.qinjee.masterdata.model.vo.organization.PostPageVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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
    List<Post> getPostList(@Param("postPageVo") PostPageVo postPageVo, @Param("sortFieldStr") String sortFieldStr, @Param("archiveId") Integer archiveId);

    /**
     * 根据机构id查询机构下的岗位
     *
     * @param orgId
     * @return
     */
    List<Post> getPostListByOrgId(@Param("orgId") Integer orgId, @Param("isEnable") Short isEnable);

    /**
     * 查询机构的岗位编码
     *
     * @param orgId
     * @return
     */
    String getLastPostByOrgId(Integer orgId);

    /**
     * 通过机构id获取岗位列表含有职位名称
     *
     * @param orgId
     * @return
     */
    List<Post> getPostPositionListByOrgId(Integer orgId);

    /**
     * 查找对应的岗位
     *
     * @param postCode
     * @param postName
     * @return
     */
    Post getPost(@Param("postCode") String postCode, @Param("postName") String postName);

    /**
     * 根据机构id查询机构下的岗位
     *
     * @param orgId
     * @return
     */
    List<Integer> getPostByOrgId(Integer orgId);
}
