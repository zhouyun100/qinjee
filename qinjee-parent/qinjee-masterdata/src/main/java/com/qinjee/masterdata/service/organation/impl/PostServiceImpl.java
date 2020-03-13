package com.qinjee.masterdata.service.organation.impl;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.organation.*;
import com.qinjee.masterdata.dao.staffdao.preemploymentdao.PreEmploymentDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchivePostRelationDao;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.vo.organization.PositionLevelVo;
import com.qinjee.masterdata.model.vo.organization.PostVo;
import com.qinjee.masterdata.model.vo.organization.bo.PostCopyBO;
import com.qinjee.masterdata.model.vo.organization.bo.PostExportBO;
import com.qinjee.masterdata.model.vo.organization.bo.PostPageBO;
import com.qinjee.masterdata.model.vo.staff.UserArchiveVo;
import com.qinjee.masterdata.redis.RedisClusterService;
import com.qinjee.masterdata.service.organation.AbstractOrganizationHelper;
import com.qinjee.masterdata.service.organation.PostService;
import com.qinjee.masterdata.utils.DealHeadParamUtil;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 彭洪思
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月23日 11:07:00
 */
@Service
public class PostServiceImpl extends AbstractOrganizationHelper<Post, Post> implements PostService {

    private static Logger logger = LogManager.getLogger(PostServiceImpl.class);

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
    private RedisClusterService redisService;
    @Autowired
    private UserArchivePostRelationDao userArchivePostRelationDao;
    @Autowired
    private UserArchiveDao userArchiveDao;
    @Autowired
    private PreEmploymentDao preEmploymentDao;
    @Autowired
    private PositionDao positionDao;
    @Autowired
    private PositionLevelDao positionLevelDao;



    @Override
    public PageResult<Post> getPostConditionPage(UserSession userSession, PostPageBO postPageBO) {
        //TODO id重复无影响
        List<Integer> orgIdList = null;
        List<Integer> postIdList = null;
        String whereSql = DealHeadParamUtil.getWhereSql(postPageBO.getTableHeadParamList(), "lastTable.");
        String orderSql = DealHeadParamUtil.getOrderSql(postPageBO.getTableHeadParamList(), "lastTable.");

        //如果postId>0,则根据postId+orgId查询岗位
        if (null != postPageBO.getPostId() && postPageBO.getPostId() != 0) {
            //找出岗位id及子岗位id集合
            postIdList = postDao.getPostIds(postPageBO.getPostId(), postPageBO.getIsEnable());

        } else {//否则只根据机构id查询 机构及子机构下所有岗位
            //如果机构id>0，则进行筛选子机构id，否则默认为全部就行了
            if (null != postPageBO.getOrgId() && postPageBO.getOrgId() > 0) {
                orgIdList = getOrgIdList(userSession, postPageBO.getOrgId(), postPageBO.getIsEnable());
            }
        }

        if (postPageBO.getCurrentPage() != null && postPageBO.getPageSize() != null) {
            PageHelper.startPage(postPageBO.getCurrentPage(), postPageBO.getPageSize());
        }
        List<Post> postList = postDao.listPostsByCondition(postPageBO, orgIdList, postIdList, whereSql, orderSql);
        //设置岗位职级列表
        handleLevelForPostList(postList);
        PageInfo<Post> pageInfo = new PageInfo<>(postList);
        PageResult<Post> pageResult = new PageResult<>(pageInfo.getList());
        pageResult.setTotal(pageInfo.getTotal());
        return pageResult;
    }

    private void handleLevelForPostList(List<Post> postList) {
        for (Post post : postList) {
            List<PositionLevel> positionLevels = postDao.listPositionLevel(post.getPostId());
            String positionLevelName = positionLevels.stream().map(a -> a.getPositionLevelName()).collect(Collectors.joining(","));
            post.setPositionLevelName(positionLevelName);
            post.setPositionLevelIds(positionLevels.stream().map(a -> a.getPositionLevelId()).collect(Collectors.toList()));
        }
    }


    @Transactional
    @Override
    public void addPost(PostVo postVo, UserSession userSession) {

        //校验orgCode是否已存在
        Post postByPostCode = postDao.getPostsByPostCode(postVo.getPostCode(), userSession.getCompanyId());
        if (Objects.nonNull(postByPostCode)) {
            ExceptionCast.cast(CommonCode.CODE_USED);
        }

        Post post = new Post();
        BeanUtils.copyProperties(postVo, post);
        Integer orgId = postVo.getOrgId();
        Integer sortId = generatePostSortId(orgId, postVo.getParentPostId());
        post.setSortId(sortId);
        post.setCompanyId(userSession.getCompanyId());
        post.setOperatorId(userSession.getArchiveId());
        post.setIsDelete((short) 0);
        post.setIsEnable((short) 1);
        postDao.insertSelective(post);
        //新增岗位职级关系表信息
        if(!CollectionUtils.isEmpty(postVo.getPositionLevelIds())){
            postDao.batchInsertPostLevelRelation(postVo.getPositionLevelIds(), userSession.getArchiveId(), post.getPostId());
        }

    }

    private Integer generatePostSortId(Integer orgId, Integer parentPostId) {
        Integer sortId = 1000;
        List<Post> sonPosts = postDao.listPostByParentPostId(parentPostId);
        List<Post> sonPostsByOrgId = postDao.listPostsByOrgIdAndEnable(orgId, null);
        if (!CollectionUtils.isEmpty(sonPostsByOrgId)) {
            int maxSortId = sonPostsByOrgId.stream().mapToInt(Post::getSortId).max().getAsInt();
            sortId = maxSortId + 1000;
        }
        //如果有上级岗位则以上级岗位下的子岗位为准
        if (!CollectionUtils.isEmpty(sonPosts)) {
            int maxSortId = sonPosts.stream().mapToInt(Post::getSortId).max().getAsInt();
            sortId = maxSortId + 1000;
        }
        return sortId;
    }

    @Override
    public String generatePostCode(Integer orgId, Integer parentPostId) {
        logger.info("根据机构id生成岗位编码：orgId=" + orgId);
        //TODO 不需要根据父岗位编码
        List<Post> sonPostsByOrgId = postDao.listPostsByOrgIdAndEnable(orgId, null);
        if (CollectionUtils.isEmpty(sonPostsByOrgId)) {
            OrganizationVO superOrg = organizationDao.getOrganizationById(orgId);
            return superOrg.getOrgCode() + "01";
        } else {
            //先过滤掉机构编码最后两位为非数字的，再筛选最大值
            List<Post> filterBrotherPostList = sonPostsByOrgId.stream().filter(o -> (o.getPostCode().length() > 2 && StringUtils.isNumeric(o.getPostCode().substring(o.getPostCode().length() - 2)))).collect(Collectors.toList());
            logger.info("滤掉后的filterBrotherPostList：" + filterBrotherPostList);
            //根据机构编码排序，并且只取最后两位位数字的
            Comparator comparator = new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    String postCode1 = String.valueOf(o1);
                    String postCode2 = String.valueOf(o2);
                    String postCode1Num = postCode1.substring(postCode1.length() - 2);
                    String postCode2Num = postCode2.substring(postCode2.length() - 2);
                    boolean isNum = StringUtils.isNumeric(postCode1Num) && StringUtils.isNumeric(postCode2Num);
                    if (isNum && postCode1.length() > postCode2.length()) {
                        return -1;
                    } else if (isNum && postCode1.length() < postCode2.length()) {
                        return 1;
                    }
                    return postCode1.compareTo(postCode2);
                }
            };
            String lastPostCode = filterBrotherPostList.stream().map(Post::getPostCode).max(comparator).get().toString();
            logger.info("当前机构下的lastPostCode：" + lastPostCode);
            if (null == lastPostCode || "".equals(lastPostCode)) {
                OrganizationVO superOrg = organizationDao.getOrganizationById(orgId);
                return superOrg.getOrgCode() + "01";
            }
            //计算编码
            String postCode = culPostCode(lastPostCode);
            logger.info("计算生成的postCode：" + postCode);
            return postCode;
        }

    }

    @Override
    public Post getPostById(String postId) {
        Post post = postDao.getPostByPostId(postId);
        List<PositionLevel> positionLevels = postDao.listPositionLevel(post.getPostId());
        String positionLevelName = positionLevels.stream().map(a -> a.getPositionLevelName()).collect(Collectors.joining(","));
        post.setPositionLevelName(positionLevelName);
        post.setPositionLevelIds(positionLevels.stream().map(a -> a.getPositionLevelId()).collect(Collectors.toList()));
        return post;
    }


    @Override
    @Transactional
    public void editPost(PostVo postVo, UserSession userSession) {

        //判断机构编码是否唯一
        Post post = new Post();
        Post postByPostCode = postDao.getPostsByPostCode(postVo.getPostCode(), userSession.getCompanyId());
        if (Objects.nonNull(postByPostCode) && !postVo.getPostId().equals(postByPostCode.getPostId())) {
            ExceptionCast.cast(CommonCode.CODE_USED);
        }
        BeanUtils.copyProperties(postVo, post);
        post.setOperatorId(userSession.getArchiveId());
        Post post1 = postDao.getPostById(postVo.getPostId());
        if (!postVo.getOrgId().equals(post1.getOrgId()) || !postVo.getParentPostId().equals(post1.getParentPostId())) {
            Integer sortId = generatePostSortId(postVo.getOrgId(), postVo.getParentPostId());
            post.setSortId(sortId);
        }
        postDao.updateByPrimaryKeySelective(post);
        //维护岗位职级
        /** 方案一
         * 1.如果编辑后的职级id数比原有的多，则需要新增多出来的，其余的做update
         * 2.如果编辑后的职级id数比原有的少，少几条就删几条，其余的做update
         * 3.如果编辑后的职级id数比原有的相等，则只要做删除
         */
        /**
         * 方案二
         * 每次编辑岗位时，逻辑删除所有旧职级，再重新新增
         */
        int i = postDao.batchDeletePostLevelRelation(userSession.getArchiveId(), post.getPostId());
        postDao.batchInsertPostLevelRelation(postVo.getPositionLevelIds(), userSession.getArchiveId(), post.getPostId());
    }

    @Transactional
    @Override
    public void deletePost(UserSession userSession, List<Integer> postIds) {
        //TODO 被删除的岗位下不允许有人员档案
        List<UserArchiveVo> userArchiveList = userArchiveDao.listUserArchiveByPostIds(postIds);
        if (!CollectionUtils.isEmpty(userArchiveList)) {
            ExceptionCast.cast(CommonCode.EXIST_USER);
        }
        //TODO 被删除的岗位下不允许有预入职人员档案
        List<PreEmployment> preEmploymentList=preEmploymentDao.selectByPostIds(postIds);
        if (!CollectionUtils.isEmpty(preEmploymentList)) {
            ExceptionCast.cast(CommonCode.EXIST_USER);
        }
        if (!CollectionUtils.isEmpty(postIds)) {
            for (Integer postId : postIds) {
                Post post = new Post();
                post.setOperatorId(userSession.getArchiveId());
                post.setPostId(postId);
                post.setIsDelete((short) 1);
                postDao.updateByPrimaryKeySelective(post);

            }
        }
    }

    @Transactional
    @Override
    public void sealPostByIds(List<Integer> postIds, Short isEnable, UserSession userSession) {
        if (!CollectionUtils.isEmpty(postIds)) {
            for (Integer postId : postIds) {
                Post post = new Post();
                post.setPostId(postId);
                post.setIsEnable(isEnable);
                post.setOperatorId(userSession.getArchiveId());
                postDao.updateByPrimaryKeySelective(post);
            }
        }
    }

    @Transactional
    @Override
    public void sortPorts(List<Integer> postIds, UserSession userSession) {
        List<Post> postList = postDao.listPostsByPostIds(postIds, null, null);
        Set<Integer> parentPostSet = new HashSet<>();
        for (Post post : postList) {
            parentPostSet.add(post.getParentPostId());
        }
        //判断是否在同一级机构下
        if (parentPostSet.size() > 1) {
            ExceptionCast.cast(CommonCode.NOT_SAME_LEVEL_EXCEPTION);
        }
        postDao.sortPorts(postIds);
    }

    @Transactional
    @Override
    public void copyPost(PostCopyBO postCopyBO, UserSession userSession) {
        List<Integer> postIds = postCopyBO.getPostIds();
        Integer orgId = postCopyBO.getOrgId();
        if (!CollectionUtils.isEmpty(postIds)) {
            for (Integer postId : postIds) {
                Post post = postDao.getPostById(postId);
                post.setOrgId(orgId);
                post.setParentPostId(0);
                post.setOperatorId(userSession.getArchiveId());
                Integer sortId = generatePostSortId(orgId, null);
                post.setSortId(sortId);
                //TODO post.setPostCode();
                postDao.insertSelective(post);
                //维护岗位职级
                List<Integer> positionLevelIds = postDao.listPositionLevelId(postId);
                postDao.batchInsertPostLevelRelation(positionLevelIds, userSession.getArchiveId(), post.getPostId());
                //岗位说明书
                PostInstructions postInstructions = postInstructionsDao.getPostInstructionsByPostId(postId);
                if (Objects.nonNull(postInstructions)) {
                    postInstructions.setOperatorId(userSession.getArchiveId());
                    postInstructions.setPostId(post.getPostId());
                    postInstructionsDao.insertSelective(postInstructions);
                }
            }
        }
    }


    /**
     * 搜集机构下所有子机构的id
     *
     * @param userSession
     * @param orgId
     * @return
     */
    private List<Integer> getOrgIdList(UserSession userSession, Integer orgId, Short isEnable) {
        return organizationDao.getOrgIds(orgId, userSession.getArchiveId(), isEnable, new Date());
    }


    @Override
    public List<UserArchivePostRelation> getPostSuccessive(Integer postId) {

        return postDao.getPostSuccessive(postId);
    }

    @Override
    public List<Post> exportPost(PostExportBO postExportBO, UserSession userSession) {
        String whereSql = DealHeadParamUtil.getWhereSql(postExportBO.getTableHeadParamList(), "tp.");
        String orderSql = DealHeadParamUtil.getOrderSql(postExportBO.getTableHeadParamList(), "tp.");
        if (CollectionUtils.isEmpty(postExportBO.getPostIds())) {
            List<Integer> orgIdList = getOrgIdList(userSession, postExportBO.getOrgId(), null);
            List<Post> posts = postDao.listPostsByOrgIds(orgIdList, whereSql, orderSql);
            //设置职级
            handleLevelForPostList(posts);
            return posts;
        } else {
            List<Post> posts = postDao.listPostsByPostIds(postExportBO.getPostIds(), whereSql, orderSql);
            handleLevelForPostList(posts);
            return posts;
        }
    }

    @Override
    @Transactional
    public ResponseResult uploadAndCheck(MultipartFile multfile, UserSession userSession) throws Exception {
        return doUploadAndCheck(multfile, Post.class, userSession);
    }

    @Override
    @Transactional
    public void importToDatabase(String redisKey, UserSession userSession) {
        String data = redisService.get(redisKey.trim());
        //postDao.getAllPostsByCompanyId(userSession.getCompanyId());
        //将其转为对象集合
        List<Post> list = JSONArray.parseArray(data, Post.class);
        LinkedMultiValueMap<String, Post> multiValuedMap = new LinkedMultiValueMap<>();
        for (Post post : list) {
            if (null == post.getParentPostCode() || "".equals(post.getParentPostCode())) {
                multiValuedMap.add(post.getPostCode(), post);
            } else {
                multiValuedMap.add(post.getParentPostCode(), post);
            }
        }
        for (Map.Entry<String, List<Post>> entry : multiValuedMap.entrySet()) {
            List<Post> orgLost = entry.getValue();
            orgLost.sort(new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    Post post1 = (Post) o1;
                    Post post2 = (Post) o2;
                    if (post1.getPostCode().length() > post1.getPostCode().length()) {
                        return 1;
                    } else if (post1.getPostCode().length() < post1.getPostCode().length()) {
                        return -1;
                    }
                    return post1.getPostCode().compareTo(post2.getPostCode());
                    //return Long.compare(Long.parseLong(post1.getPostCode()), Long.parseLong(post2.getPostCode()));
                }
            });
            //TODO 45
            int sortId = 1000;
            for (Post vo : orgLost) {
                Post ifExistVo = postDao.getPostsByPostCode(vo.getPostCode(), userSession.getCompanyId());
                Post parentPost = postDao.getPostsByPostCode(vo.getParentPostCode(), userSession.getCompanyId());
                OrganizationVO orgVo = organizationDao.getOrganizationByOrgCodeAndCompanyId(vo.getOrgCode(), userSession.getCompanyId());

                //设置所属机构id
                vo.setOrgId(orgVo.getOrgId());
                //设置职位id
                if (StringUtils.isNotEmpty(vo.getPositionName())) {
                    Position position = positionDao.getPositionByNameAndCompanyId(vo.getPositionName(), userSession.getCompanyId());
                    vo.setPositionId(position.getPositionId());
                }

                //设置上级岗位id
                if (Objects.nonNull(parentPost)) {
                    vo.setParentPostId(parentPost.getPostId());
                } else {
                    vo.setParentPostId(0);
                }
                //已存在 则更新
                if (Objects.nonNull(ifExistVo)) {
                    postDao.updateByPrimaryKeySelective(vo);
                    vo.setPostId(ifExistVo.getPostId());

                } else {
                    vo.setOperatorId(userSession.getArchiveId());
                    vo.setCompanyId(userSession.getCompanyId());
                    vo.setSortId(sortId);
                    sortId += 1000;
                    int i = postDao.insertSelective(vo);
                }
                //维护职级
                int i = postDao.batchDeletePostLevelRelation(userSession.getArchiveId(), vo.getPostId());
                if (StringUtils.isNotEmpty(vo.getPositionLevelName())) {
                    String[] plNames = vo.getPositionLevelName().split(",");
                    if (plNames.length > 0) {
                        List<PositionLevelVo> positionLevelList = positionLevelDao.getByPositionLevelNames(Arrays.asList(plNames), userSession.getCompanyId());
                        if (!CollectionUtils.isEmpty(positionLevelList)) {
                            //取出id
                            List<Integer> plIds = positionLevelList.stream().map(pl -> pl.getPositionLevelId()).collect(Collectors.toList());
                            postDao.batchInsertPostLevelRelation(plIds, userSession.getArchiveId(), vo.getPostId());
                        }
                    }
                }

            }
        }
    }

    @Override
    public List<Post> getPostGraphics(UserSession userSession, Integer layer, boolean isContainsCompiler, boolean isContainsActualMembers, Integer postId, Short isEnable) {
        //拿到关联的所有机构id
        List<Integer> postIdList = null;
        if (layer < 1) {
            layer = 2;//TODO 暂时没用到
        }
        postIdList = postDao.getPostIds(postId, isEnable);
        //查询所有相关的岗位
        List<Post> allPost = postDao.getPostGraphics(postIdList, isEnable);

        if (CollectionUtils.isEmpty(allPost)) {
            //不存在相关岗位异常
            ExceptionCast.cast(CommonCode.POST_NOT_EXSIT_EXCEPTION);
        }

        //拿到根节点，以及两位上级节点
        List<Post> topPostList = allPost.stream().filter(post -> {
            if (post.getPostId() != null && post.getPostId().equals(postId)) {
                return true;
            } else if (postId == 0) {//TODO 如果是顶级岗位
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        //递归处理机构,使其以树形结构展示
        //allPost最后只会有一个元素
        handlerPostToGraphics(allPost, topPostList, isContainsCompiler, isContainsActualMembers);
        //TODO 拿出根节点，设置上两级父级岗位
        Post parentPost = postDao.getPostById(allPost.get(0).getParentPostId());

        if (Objects.nonNull(parentPost)) {
            parentPost.setChildList(allPost);
            List<Post> pList = new ArrayList<>();
            pList.add(parentPost);
            Post parentPost2 = postDao.getPostById(parentPost.getParentPostId());
            if (Objects.nonNull(parentPost2)) {
                parentPost2.setChildList(pList);
                List<Post> pList2 = new ArrayList<>();
                pList2.add(parentPost2);
                return pList2;
            }
            return pList;
        }
        return allPost;
    }

    @Override
    public PageResult<Post> listDirectPostPage(PostPageBO postPageBO) {

        if (postPageBO.getCurrentPage() != null && postPageBO.getPageSize() != null) {
            PageHelper.startPage(postPageBO.getCurrentPage(), postPageBO.getPageSize());
        }
        String whereSql = DealHeadParamUtil.getWhereSql(postPageBO.getTableHeadParamList(), "lastTable.");
        String orderSql = DealHeadParamUtil.getOrderSql(postPageBO.getTableHeadParamList(), "lastTable.");
        List<Post> postList = postDao.listDirectPostPage(postPageBO, whereSql, orderSql);
        handleLevelForPostList(postList);
        PageInfo<Post> pageInfo = new PageInfo<>(postList);
        PageResult<Post> pageResult = new PageResult<>(pageInfo.getList());
        pageResult.setTotal(pageInfo.getTotal());
        return pageResult;
    }

    @Override
    @Transactional
    public void cancelImport(String redisKey, String errorInfoKey) {
        redisService.del(redisKey);
        redisService.del(errorInfoKey);
    }


    private void handlerPostToGraphics(List<Post> allPost, List<Post> topPostList, boolean isContainsCompiler, boolean isContainsActualMembers) {
        for (Post post : topPostList) {
            Integer postId = post.getPostId();
            //设置实有人数
            if (isContainsActualMembers) {
                post.setStaffNumbers(userArchiveDao.countUserArchiveByPostId(postId));
            }
            //TODO 设置编制人数,先写死
            if (isContainsCompiler) {
                post.setPlanNumbers(120);
            }
            List<Post> childList = allPost.stream().filter(pt -> {
                Integer postParentId = pt.getParentPostId();
                if (postParentId != null && postParentId >= 0) {
                    return postParentId.equals(postId);
                }
                return false;
            }).collect(Collectors.toList());
            //判断是否还有子级
            if (childList != null && childList.size() > 0) {
                post.setChildList(childList);
                allPost.removeAll(childList);
                handlerPostToGraphics(allPost, childList, isContainsCompiler, isContainsActualMembers);
            }
        }


    }


    /**
     * 删除岗位职等关系表信息
     *
     * @param userSession
     * @param postGradeRelation
     */
    private void deletePostGradeRelation(UserSession userSession, PostGradeRelation postGradeRelation) {
        postGradeRelation.setIsDelete((short) 1);
        postGradeRelation.setOperatorId(userSession.getArchiveId());
        postGradeRelationDao.updateByPrimaryKeySelective(postGradeRelation);
    }


    /**
     * 删除岗位职级关系信息
     *
     * @param userSession
     * @param postLevelRelation
     */
    private void deletePostLevelRelation(UserSession userSession, PostLevelRelation postLevelRelation) {
        postLevelRelation.setIsDelete((short) 1);
        postLevelRelation.setOperatorId(userSession.getArchiveId());
        postLevelRelationDao.updateByPrimaryKeySelective(postLevelRelation);
    }


    private String culPostCode(String orgCode) {
        String number = orgCode.substring(orgCode.length() - 2);
        String preCode = orgCode.substring(0, orgCode.length() - 2);
        Integer new_postCode = Integer.parseInt(number) + 1;
        String code = new_postCode.toString();
        int i = 2 - code.length();
        if (i < 0) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        for (int k = 0; k < i; k++) {
            code = "0" + code;
        }
        String newPostCode = preCode + code;
        return newPostCode;
    }

    @Override
    protected List<Post> checkExcel(List<Post> dataList, UserSession userSession) {
        dataList.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Post org1 = (Post) o1;
                Post org2 = (Post) o2;
                return org1.getLineNumber().compareTo(org2.getLineNumber());
                //return Long.compare(Long.parseLong(org1.getOrgCode()), Long.parseLong(org2.getOrgCode()));
            }
        });
        List<OrganizationVO> organizationVOListMem = organizationDao.listOrganizationByCompanyId(userSession.getCompanyId());
        List<Post> postMem = postDao.listPostsByCompanyId(userSession.getCompanyId());
        List<Position> positionListMem = positionDao.getPositionListByCompanyId(userSession.getCompanyId());

        List<Post> checkVos = new ArrayList<>(dataList.size());
        //用来校验本地excel岗位编码与名称是否匹配，如果匹配 还要进行数据库校验
        Map<String, String> excelPostNameMap = new HashMap<>(dataList.size());
        for (Post post : dataList) {
            excelPostNameMap.put(post.getPostCode(), post.getPostName());
        }
        List<PositionLevelVo> positionLevelsMem = positionLevelDao.list(userSession.getCompanyId());
        for (Post post : dataList) {
            // Post checkVo = new Post();
            // BeanUtils.copyProperties(post, checkVo);
            StringBuilder resultMsg = new StringBuilder(1024);
            //验空
            if (StringUtils.isBlank(post.getPostCode())) {
                post.setCheckResult(false);
                resultMsg.append("岗位编码不能为空 | ");
            }
            if (StringUtils.isBlank(post.getPostName())) {
                post.setCheckResult(false);
                resultMsg.append("岗位名称不能为空 | ");
            }
            if (StringUtils.isBlank(post.getOrgCode())) {
                post.setCheckResult(false);
                resultMsg.append("所属部门编码不能为空 | ");
            }
            if (StringUtils.isBlank(post.getOrgName())) {
                post.setCheckResult(false);
                resultMsg.append("所属部门名称不能为空 | ");
            }

            if (StringUtils.isNotBlank(post.getPositionLevelName())) {
                String[] plNames = post.getPositionLevelName().split(",");
                StringBuilder sb = new StringBuilder();
                Arrays.stream(plNames).forEach(plName -> {
                    boolean bool = positionLevelsMem.stream().anyMatch(pl -> plName.equals(pl.getPositionLevelName()));
                    if (!bool) {
                        sb.append(plName).append(",");
                    }
                });
                if (sb.length() > 0) {
                    String mes = sb.substring(0, sb.length() - 1);
                    post.setCheckResult(false);
                    resultMsg.append("职级[" + mes + "] 不存在| ");
                }
            }

            // 校验部门编码是否存在，部门编码与部门名称是否对应
            if (StringUtils.isNotBlank(post.getOrgCode())) {
                boolean bool = organizationVOListMem.stream().anyMatch(a -> a.getOrgCode().equals(post.getOrgCode()));
                if (bool) {
                    if (!organizationVOListMem.stream().anyMatch(a -> a.getOrgName().equals(post.getOrgName()))) {
                        post.setCheckResult(false);
                        resultMsg.append("部门名称不匹配 | ");
                    }
                } else {
                    post.setCheckResult(false);
                    resultMsg.append("部门编码[" + post.getOrgCode() + "]不存在 | ");
                }
            }
            if (StringUtils.isNotBlank(post.getParentPostCode())) {
                String parentPostName = excelPostNameMap.get(post.getParentPostCode());
                if (StringUtils.isNotBlank(parentPostName)) {
                    if (!parentPostName.equals(post.getParentPostName())) {
                        post.setCheckResult(false);
                        resultMsg.append("上级岗位名称在excel中不匹配 | ");
                    }
                } else {
                    // 校验上级岗位编码是否存在，与岗位名称是否对应
                    boolean bool = postMem.stream().anyMatch(a -> post.getParentPostCode().equals(a.getParentPostCode()));
                    if (bool) {
                        if (StringUtils.isNotBlank(post.getParentPostName())) {
                            if (!postMem.stream().anyMatch(a -> post.getParentPostName().equals(a.getParentPostName()))) {
                                post.setCheckResult(false);
                                resultMsg.append("上级岗位名称在数据库中不匹配 | ");
                            }
                        }
                    } else {
                        post.setCheckResult(false);
                        resultMsg.append("上级岗位编码为[" + post.getParentPostCode() + "]的岗位不存在 | ");
                    }
                }
            }
            if (StringUtils.isNotBlank(post.getPositionName())) {
                //校验职位是否存在
                boolean bool = positionListMem.stream().anyMatch(a -> post.getPositionName().equals(a.getPositionName()));
                if (!bool) {
                    post.setCheckResult(false);
                    resultMsg.append("职位[" + post.getPositionName() + "]不存在 | ");
                }
            }

            if (resultMsg.length() > 2) {
                resultMsg.deleteCharAt(resultMsg.length() - 2);
            }
            post.setResultMsg(resultMsg.toString());
            if (!post.isCheckResult()) {
                checkVos.add(post);
            }
        }
        organizationVOListMem = null;
        postMem = null;
        positionListMem = null;
        //用来校验本地excel岗位编码与名称是否匹配，如果匹配 还要进行数据库校验
        excelPostNameMap = null;
        return checkVos;
    }
}
