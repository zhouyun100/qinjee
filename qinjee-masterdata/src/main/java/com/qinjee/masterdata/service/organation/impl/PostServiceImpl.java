package com.qinjee.masterdata.service.organation.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.masterdata.dao.*;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchivePostRelationDao;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.organization.PostPageVo;
import com.qinjee.masterdata.model.vo.organization.PostVo;
import com.qinjee.masterdata.model.vo.organization.QueryFieldVo;
import com.qinjee.masterdata.service.organation.PostService;
import com.qinjee.masterdata.utils.QueryFieldUtil;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月23日 11:07:00
 */
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostDao postDao;
    @Autowired
    private PostLevelRelationDao postLevelRelationDao;
    @Autowired
    private PostGradeRelationDao postGradeRelationDao;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private PostInstructionsDao postInstructionsDao;

    @Autowired
    private UserArchivePostRelationDao userArchivePostRelationDao;

    @Override
    public ResponseResult<PageResult<Post>> getPostList(UserSession userSession, PostPageVo postPageVo) {
        Integer archiveId = userSession.getArchiveId();
        Optional<List<QueryFieldVo>> querFieldVos = Optional.of(postPageVo.getQuerFieldVos());
        String sortFieldStr = QueryFieldUtil.getSortFieldStr(querFieldVos, Post.class);
        if(postPageVo.getCurrentPage() != null && postPageVo.getPageSize() != null){
            PageHelper.startPage(postPageVo.getCurrentPage(),postPageVo.getPageSize());
        }
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

    @Transactional
    @Override
    public ResponseResult addPost(PostVo postVo, UserSession userSession) {
        Post post = new Post();
        BeanUtils.copyProperties(postVo, post);
        Integer orgId = postVo.getOrgId();
        //查询机构的岗位生成岗位编码
        String postCode = getPostCode(orgId);
        //获取排序sortId
        Integer sortId = getPostSortId(orgId);

        post.setPostCode(postCode);
        post.setCompanyId(userSession.getCompanyId());
        post.setOperatorId(userSession.getArchiveId());
        post.setSortId(sortId);
        post.setIsDelete((short) 0);
        post.setIsEnable((short) 1);
        postDao.insertSelective(post);

        //根据职级职等插入岗位职等,岗位职级信息
        //新增岗位职级关系表信息
        addPostLevelAndGradeRelation(postVo, userSession, post);
        return new ResponseResult();
    }

    @Override
    public ResponseResult editPost(PostVo postVo, UserSession userSession) {
        Post post = new Post();
        BeanUtils.copyProperties(postVo, post);
        post.setOperatorId(userSession.getArchiveId());
        postDao.updateByPrimaryKeySelective(post);
        //删除修改不含有的岗位职级关系信息
        deletePostLevel(postVo, userSession, post);
        //删除修改不含有的岗位职等关系信息
        deletePostGrade(postVo, userSession, post);
        //新增岗位职级关系表信息
        addPostLevelAndGradeRelation(postVo, userSession, post);

        return new ResponseResult();
    }

    @Transactional
    @Override
    public ResponseResult deletePost(UserSession userSession, List<Integer> postIds) {
        //TODO 被删除的岗位下不允许有人员档案
        if(!CollectionUtils.isEmpty(postIds)){
            for (Integer postId : postIds) {
                Post post = new Post();
                post.setOperatorId(userSession.getArchiveId());
                post.setPostId(postId);
                post.setIsDelete((short) 1);
                postDao.updateByPrimaryKeySelective(post);

                List<PostLevelRelation> postLevelRelationList = postLevelRelationDao.getPostLevelRelationByPostId(post.getPostId());
                //删除岗位职级关系信息
                if(!CollectionUtils.isEmpty(postLevelRelationList)){
                    for (PostLevelRelation postLevelRelation : postLevelRelationList) {
                        deletePostLevelRelation(userSession,postLevelRelation);
                    }
                }

                List<PostGradeRelation> postGradeRelationList = postGradeRelationDao.getPostGradeRelationByPostId(post.getPostId());
                //删除岗位职等关系信息
                if(!CollectionUtils.isEmpty(postGradeRelationList)){
                    for (PostGradeRelation postGradeRelation : postGradeRelationList) {
                        deletePostGradeRelation(userSession,postGradeRelation);
                    }
                }
            }
        }
        return new ResponseResult();
    }

    @Transactional
    @Override
    public ResponseResult sealPostByIds(List<Integer> postIds, Short isEnable, UserSession userSession) {
       if(!CollectionUtils.isEmpty(postIds)){
           for (Integer postId : postIds) {
               Post post = new Post();
               post.setPostId(postId);
               post.setIsEnable(isEnable);
               post.setOperatorId(userSession.getArchiveId());
               postDao.updateByPrimaryKeySelective(post);
           }
       }
        return new ResponseResult();
    }

    @Transactional
    @Override
    public ResponseResult sortOrganization(Integer prePostId, Integer midPostId, Integer nextPostId, UserSession userSession) {
        Post prePost;
        Post nextPost;
        Integer midSort = null;

        if(nextPostId != null){
            //移动
            nextPost = postDao.selectByPrimaryKey(nextPostId);
            midSort = nextPost.getSortId() - 1;
        }else if(nextPostId == null){
            //移动到最后
            prePost = postDao.selectByPrimaryKey(prePostId);
            midSort = prePost.getSortId() + 1;
        }
        Post post = new Post();
        post.setSortId(midSort);
        post.setOperatorId(userSession.getArchiveId());
        post.setSortId(midSort);
        postDao.updateByPrimaryKeySelective(post);
        return new ResponseResult();
    }

    @Transactional
    @Override
    public ResponseResult copyPost(List<Integer> postIds, UserSession userSession, Integer orgId) {
        if(!CollectionUtils.isEmpty(postIds)){
            String postCode = getPostCode(orgId);
            Integer postSortId = getPostSortId(orgId);
            for (Integer postId : postIds) {
                postSortId += 1000;
                Integer integer = Integer.valueOf(postCode);
                postCode = String.valueOf(integer + 1);
                Post post = postDao.selectByPrimaryKey(postId);
                post.setOrgId(orgId);
                post.setPostCode(postCode);
                post.setParentPostId(null);
                post.setOperatorId(userSession.getArchiveId());
                post.setSortId(postSortId);
                postDao.insertSelective(post);

                //岗位说明书
                PostInstructions postInstructions = postInstructionsDao.getPostInstructionsByPostId(post.getPostId());
                postInstructions.setOperatorId(userSession.getArchiveId());
                postInstructions.setPostId(post.getPostId());
                postInstructionsDao.insertSelective(postInstructions);
//                //岗位职等关系表
//                List<PostGradeRelation> postGradeRelationList = postGradeRelationDao.getPostGradeRelationByPostId(post.getPostId());
//                //岗位职级关系表
//                List<PostLevelRelation> postLevelRelationList = postLevelRelationDao.getPostLevelRelationByPostId(post.getPostId());
            }
        }
        return new ResponseResult();
    }

    @Override
    public ResponseResult<List<Post>> getAllPost(UserSession userSession, Integer orgId) {
        List<Post> postList = postDao.getPostPositionListByOrgId(orgId);
        return new ResponseResult<>(postList);
    }

    /**
     * 删除修改不含有的岗位职等关系信息
     * @param postVo
     * @param userSession
     * @param post
     */
    private void deletePostGrade(PostVo postVo, UserSession userSession, Post post) {
        List<PostGradeRelation> postGradeRelationList = postGradeRelationDao.getPostGradeRelationByPostId(post.getPostId());
        if(!CollectionUtils.isEmpty(postGradeRelationList)){
            List<Integer> positionGrades = postVo.getPositionGrades();
            for (PostGradeRelation postGradeRelation : postGradeRelationList) {
                if(!CollectionUtils.isEmpty(positionGrades)){
                    if(!positionGrades.contains(postGradeRelation.getPositionGradeId())){
                        //删除原来有的岗位职等关系表信息
                        deletePostGradeRelation(userSession, postGradeRelation);
                    }else {
                        //含有原来的职等
                        positionGrades.remove(postGradeRelation.getPositionGradeId());
                    }
                }else {
                    //删除原来有的岗位职等关系表信息
                    deletePostGradeRelation(userSession, postGradeRelation);
                }
            }
        }
    }

    /**
     * 删除岗位职等关系表信息
     * @param userSession
     * @param postGradeRelation
     */
    private void deletePostGradeRelation(UserSession userSession, PostGradeRelation postGradeRelation) {
        postGradeRelation.setIsDelete((short) 1);
        postGradeRelation.setOperatorId(userSession.getArchiveId());
        postGradeRelationDao.updateByPrimaryKeySelective(postGradeRelation);
    }

    /**
     * 删除修改不含有的岗位职级关系信息
     * @param postVo
     * @param userSession
     * @param post
     */
    private void deletePostLevel(PostVo postVo, UserSession userSession, Post post) {
        List<PostLevelRelation> postLevelRelationList = postLevelRelationDao.getPostLevelRelationByPostId(post.getPostId());
        if(!CollectionUtils.isEmpty(postLevelRelationList)){
            List<Integer> positionLevels = postVo.getPositionLevels();
            for (PostLevelRelation postLevelRelation : postLevelRelationList) {
               if(!CollectionUtils.isEmpty(positionLevels)){
                   if(!positionLevels.contains(postLevelRelation.getPositionLevelId())){
                       //删除原来有的
                       deletePostLevelRelation(userSession, postLevelRelation);
                   }else {
                       //含有原来的
                       positionLevels.remove(postLevelRelation.getPositionLevelId());
                   }
               }else {
                   //删除岗位职级关系表信息
                   deletePostLevelRelation(userSession, postLevelRelation);
               }
            }
        }
    }

    /**
     * 删除岗位职级关系信息
     * @param userSession
     * @param postLevelRelation
     */
    private void deletePostLevelRelation(UserSession userSession, PostLevelRelation postLevelRelation) {
        postLevelRelation.setIsDelete((short) 1);
        postLevelRelation.setOperatorId(userSession.getArchiveId());
        postLevelRelationDao.updateByPrimaryKeySelective(postLevelRelation);
    }


    /**
     * 新增岗位职级关系表信息
     * @param postVo
     * @param userSession
     * @param post
     */
    private void addPostLevelAndGradeRelation(PostVo postVo, UserSession userSession, Post post) {
        List<Integer> positionLevels = postVo.getPositionLevels();
        if(!CollectionUtils.isEmpty(positionLevels)){
            for (Integer positionLevel : positionLevels) {
                PostLevelRelation postLevelRelation = new PostLevelRelation();
                postLevelRelation.setIsDelete((short) 0);
                postLevelRelation.setOperatorId(userSession.getArchiveId());
                postLevelRelation.setPostId(post.getPostId());
                postLevelRelation.setPositionLevelId(positionLevel);
                postLevelRelationDao.insertSelective(postLevelRelation);
            }
        }


        //新增岗位职等关系表信息
        List<Integer> positionGrades = postVo.getPositionGrades();
        if(!CollectionUtils.isEmpty(positionGrades)){
            for (Integer positionGrade : positionGrades) {
                PostGradeRelation postGradeRelation = new PostGradeRelation();
                postGradeRelation.setIsDelete((short) 0);
                postGradeRelation.setOperatorId(userSession.getArchiveId());
                postGradeRelation.setPositionGradeId(positionGrade);
                postGradeRelation.setPostId(post.getPostId());
                postGradeRelationDao.insertSelective(postGradeRelation);
            }
        }
    }

    /**
     * 获取排序sortId
     * @param orgId
     * @return
     */
    private Integer getPostSortId(Integer orgId) {
        List<Post> postList = postDao.getPostListByOrgId(orgId, null);
        Integer sortId;
        if(CollectionUtils.isEmpty(postList)){
            Post lastPost = postList.get(postList.size() - 1);
            sortId = lastPost.getSortId() + 1000;
        }else {
            sortId = 1000;
        }
        return sortId;
    }

    /**
     * 查询机构的岗位生成岗位编码
     * @param orgId
     * @return
     */
    private String getPostCode(Integer orgId) {
        String postCode = postDao.getLastPostByOrgId(orgId);
        if(postCode == null){
            Organization organization = organizationDao.selectByPrimaryKey(orgId);
            String orgCode = organization.getOrgCode();
            postCode += orgCode + "001";
        }else {
            Integer integer = Integer.valueOf(postCode);
            postCode = String.valueOf(integer + 1);
        }
        return postCode;
    }


}
