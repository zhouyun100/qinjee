package com.qinjee.masterdata.service.organation.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.PostDao;
import com.qinjee.masterdata.dao.UserArchivePostRelationDao;
import com.qinjee.masterdata.model.entity.Post;
import com.qinjee.masterdata.model.entity.UserArchivePostRelation;
import com.qinjee.masterdata.model.vo.organization.PostPageVo;
import com.qinjee.masterdata.model.vo.organization.QueryFieldVo;
import com.qinjee.masterdata.service.organation.PostService;
import com.qinjee.masterdata.utils.QueryFieldUtil;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月23日 11:07:00
 */
public class PostServiceImpl implements PostService {

    @Autowired
    private PostDao postDao;

    @Autowired
    private UserArchivePostRelationDao userArchivePostRelationDao;

    @Override
    public ResponseResult<PageResult<Post>> getPostList(UserSession userSession, PostPageVo postPageVo) {
        Integer archiveId = userSession.getArchiveId();
        Optional<List<QueryFieldVo>> querFieldVos = Optional.of(postPageVo.getQuerFieldVos());
        String sortFieldStr = QueryFieldUtil.getSortFieldStr(querFieldVos, Post.class);
        PageHelper.startPage(postPageVo.getCurrentPage(),postPageVo.getPageSize());
        List<Post> postList = postDao.getPostList(postPageVo, sortFieldStr, archiveId);
        PageResult<Post> pageResult = new PageResult<>(postList);
        return new ResponseResult<>(pageResult);
    }

    @Override
    public ResponseResult<PageResult<UserArchivePostRelation>> getUserArchivePostRelationList(Integer pageSize, Integer currentPage, Integer postId) {
        if(pageSize != null && currentPage != null){
            PageHelper.startPage(currentPage, pageSize);
        }
        List<UserArchivePostRelation> userArchivePostRelationList = userArchivePostRelationDao.getUserArchivePostRelationList(postId);
        PageResult<UserArchivePostRelation> pageResult = new PageResult<>(userArchivePostRelationList);
        return new ResponseResult<>(pageResult);
    }
}
