package com.qinjee.masterdata.service.organation;

import com.qinjee.masterdata.model.entity.Post;
import com.qinjee.masterdata.model.entity.UserArchivePostRelation;
import com.qinjee.masterdata.model.vo.organization.PostPageVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月23日 11:07:00
 */
public interface PostService {
    /**
     * 分页查询岗位列表
     * @param userSession
     * @param postPageVo
     * @return
     */
    ResponseResult<PageResult<Post>> getPostList(UserSession userSession, PostPageVo postPageVo);

    /**
     * 根据岗位id查询员工档案岗位关系表
     * @param pageSize
     * @param currentPage
     * @param postId
     * @return
     */
    ResponseResult<PageResult<UserArchivePostRelation>> getUserArchivePostRelationList(Integer pageSize, Integer currentPage, Integer postId);
}
