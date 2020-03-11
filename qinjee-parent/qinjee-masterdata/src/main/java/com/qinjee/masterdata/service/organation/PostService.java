package com.qinjee.masterdata.service.organation;

import com.qinjee.masterdata.model.entity.Post;
import com.qinjee.masterdata.model.entity.UserArchivePostRelation;
import com.qinjee.masterdata.model.vo.organization.bo.PostCopyBO;
import com.qinjee.masterdata.model.vo.organization.bo.PostExportBO;
import com.qinjee.masterdata.model.vo.organization.bo.PostPageBO;
import com.qinjee.masterdata.model.vo.organization.PostVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 彭洪思
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月23日 11:07:00
 */
public interface PostService {
    /**
     * 分页查询岗位列表
     *
     * @param userSession
     * @param postPageBO
     * @return
     */
    PageResult<Post> getPostConditionPage(UserSession userSession, PostPageBO postPageBO);


    /**
     * 新增岗位
     *
     * @param postVo
     * @return
     */
    @Transactional
    void addPost(PostVo postVo, UserSession userSession);

    /**
     * 编辑岗位
     *
     * @param postVo
     * @param userSession
     * @return
     */
    @Transactional
    void editPost(PostVo postVo, UserSession userSession);

    /**
     * 删除岗位
     *
     * @param userSession
     * @param postIds
     * @return
     */
    void deletePost(UserSession userSession, List<Integer> postIds);

    /**
     * 解封/封存机构
     *
     * @param postIds
     * @param isEnable
     * @param userSession
     * @return
     */
    void sealPostByIds(List<Integer> postIds, Short isEnable, UserSession userSession);


    void sortPorts(List<Integer> postIds, UserSession userSession);

    /**
     * 复制岗位
     *
     * @param postCopyBO
     * @param userSession
     * @return
     */
    void copyPost(PostCopyBO postCopyBO, UserSession userSession);


    /**
     * 查看岗位历任
     *
     * @return
     */
    List<UserArchivePostRelation> getPostSuccessive(Integer postId);

    List<Post> exportPost(PostExportBO postExportBO, UserSession userSession);

    ResponseResult uploadAndCheck(MultipartFile multfile, UserSession userSession) throws Exception;

    @Transactional
    void importToDatabase(String redisKey, UserSession userSession);

    List<Post> getPostGraphics(UserSession userSession, Integer layer, boolean isContainsCompiler, boolean isContainsActualMembers, Integer orgId, Short isEnable);

    /**
     * 分页查询岗位直属下级
     *
     * @param postPageBO
     * @return
     */
    PageResult<Post> listDirectPostPage(PostPageBO postPageBO);

    void cancelImport(String redisKey, String errorInfoKey);

    String generatePostCode(Integer orgId, Integer parentPostId);

    Post getPostById(String postId);
}
