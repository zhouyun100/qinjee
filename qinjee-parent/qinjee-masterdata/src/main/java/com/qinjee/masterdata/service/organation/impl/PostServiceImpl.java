package com.qinjee.masterdata.service.organation.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.*;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchiveDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchivePostRelationDao;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.vo.organization.PostVo;
import com.qinjee.masterdata.model.vo.organization.page.PostPageVo;
import com.qinjee.masterdata.model.vo.organization.query.QueryField;
import com.qinjee.masterdata.redis.RedisClusterService;
import com.qinjee.masterdata.service.organation.OrganizationService;
import com.qinjee.masterdata.service.organation.PostService;
import com.qinjee.masterdata.utils.QueryFieldUtil;
import com.qinjee.masterdata.utils.pexcel.ExcelImportUtil;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
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
    private RedisClusterService redisService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private UserArchivePostRelationDao userArchivePostRelationDao;
    @Autowired
    private UserArchiveDao userArchiveDao;
    @Autowired
    private PositionDao positionDao;

    @Override
    public PageResult<Post> getPostConditionPage(UserSession userSession, PostPageVo postPageVo) {
        String sortFieldStr = null;
        if (Objects.nonNull(postPageVo.getQuerFieldVos())) {
            Optional<List<QueryField>> querFieldVos = Optional.of(postPageVo.getQuerFieldVos());
            sortFieldStr = QueryFieldUtil.getSortFieldStr(querFieldVos, Post.class);
        }
        //TODO id重复无影响
        List<Integer> orgidList = null;
        //如果机构id不是0，则进行筛选子机构id，否则默认为全部就行了
        if (postPageVo.getOrgId() != 0) {
            orgidList = getOrgIdList(userSession, postPageVo.getOrgId());
        }
        if (postPageVo.getCurrentPage() != null && postPageVo.getPageSize() != null) {
            PageHelper.startPage(postPageVo.getCurrentPage(), postPageVo.getPageSize());
        }
        List<Post> postList = postDao.getPostConditionPages(postPageVo, orgidList, sortFieldStr);
        PageInfo<Post> pageInfo = new PageInfo<>(postList);
        PageResult<Post> pageResult = new PageResult<>(pageInfo.getList());
        pageResult.setTotal(pageInfo.getTotal());
        return pageResult;
    }

    @Override
    public ResponseResult<PageResult<UserArchivePostRelation>> getUserArchivePostRelationList(Integer pageSize, Integer currentPage, Integer postId) {
        if (pageSize != null && currentPage != null) {
            PageHelper.startPage(currentPage, pageSize);
        }
        List<UserArchivePostRelation> userArchivePostRelationList = userArchivePostRelationDao.getUserArchivePostRelationList(postId);
        PageResult<UserArchivePostRelation> pageResult = new PageResult<>(userArchivePostRelationList);
        return new ResponseResult<>(pageResult);
    }

    @Transactional
    @Override
    public void addPost(PostVo postVo, UserSession userSession) {
        Post post = new Post();
        BeanUtils.copyProperties(postVo, post);
        Integer orgId = postVo.getOrgId();
        generatePostCodeAndSortId(post, orgId, postVo.getParentPostId());
        post.setCompanyId(userSession.getCompanyId());
        post.setOperatorId(userSession.getArchiveId());
        post.setIsDelete((short) 0);
        post.setIsEnable((short) 1);
        postDao.insertSelective(post);
        //根据职级职等插入岗位职等,岗位职级信息
        //新增岗位职级关系表信息
        // addPostLevelAndGradeRelation(postVo, userSession, post);
    }

    private void generatePostCodeAndSortId(Post post, Integer orgId, Integer parentPostId) {
        String postCode = "";
        Integer sortId = 1000;
        //如果父级岗位存在 则按照父级岗位的编码为基础，否则以归属机构的为准
        Post parentPost = postDao.selectByPrimaryKey(parentPostId);
        if (Objects.nonNull(parentPost)) {
            //查询父级岗位下的子岗位列表
            List<Post> sonPosts = postDao.getPostListByPostId(parentPostId);
            if (CollectionUtils.isEmpty(sonPosts)) {
                String parentPoatCode = parentPost.getPostCode();
                postCode = parentPoatCode + "01";
            } else {
                postCode = sonPosts.get(0).getPostCode();
                postCode = culPostCode(postCode);
                sortId = sonPosts.get(0).getSortId() + 1000;
            }
        } else {
            List<Post> posts = postDao.getLastTopPostByOrgId(orgId);
            if (CollectionUtils.isEmpty(posts)) {
                //当前机构编码+2位流水
                OrganizationVO organizationVO = organizationDao.selectByPrimaryKey(orgId);
                String orgCode = organizationVO.getOrgCode();
                postCode = orgCode + "01";
            } else {
                postCode = posts.get(0).getPostCode();
                postCode = culPostCode(postCode);
                sortId = posts.get(0).getSortId() + 1000;
            }

        }
        post.setPostCode(postCode);
        post.setSortId(sortId);
    }

    @Override
    @Transactional
    public void editPost(PostVo postVo, UserSession userSession) {
        Post post = new Post();
        BeanUtils.copyProperties(postVo, post);
        post.setOperatorId(userSession.getArchiveId());

        //如果上级机构id或上级岗位id改变，则重新生成岗位编码 和 排序id
        Post post1 = postDao.selectByPrimaryKey(postVo.getPostId());
        if (!postVo.getOrgId().equals(post1.getOrgId())) {
            generatePostCodeAndSortId(post, postVo.getOrgId(), postVo.getParentPostId());
        }
        postDao.updateByPrimaryKeySelective(post);
        //删除修改不含有的岗位职级关系信息
        //deletePostLevel(postVo, userSession, post);
        //删除修改不含有的岗位职等关系信息
        //deletePostGrade(postVo, userSession, post);
        //新增岗位职级关系表信息
        //addPostLevelAndGradeRelation(postVo, userSession, post);
    }

    @Transactional
    @Override
    public void deletePost(UserSession userSession, List<Integer> postIds) {
        //TODO 被删除的岗位下不允许有人员档案
        //被引用过的岗位 不允许删除
        if (!CollectionUtils.isEmpty(postIds)) {
            for (Integer postId : postIds) {
                Post post = new Post();
                post.setOperatorId(userSession.getArchiveId());
                post.setPostId(postId);
                post.setIsDelete((short) 1);
                postDao.updateByPrimaryKeySelective(post);

              /*  List<PostLevelRelation> postLevelRelationList = postLevelRelationDao.getPostLevelRelationByPostId(post.getPostId());
                //删除岗位职级关系信息
                if(!CollectionUtils.isEmpty(postLevelRelationList)){
                    for (PostLevelRelation postLevelRelation : postLevelRelationList) {
                        deletePostLevelRelation(userSession,postLevelRelation);
                    }
                }*/

               /* List<PostGradeRelation> postGradeRelationList = postGradeRelationDao.getPostGradeRelationByPostId(post.getPostId());
                //删除岗位职等关系信息
                if(!CollectionUtils.isEmpty(postGradeRelationList)){
                    for (PostGradeRelation postGradeRelation : postGradeRelationList) {
                        deletePostGradeRelation(userSession,postGradeRelation);
                    }
                }*/
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
        List<Post> postList = postDao.getPostListByPostIds(postIds);
        Set<Integer> parentPostSet = new HashSet<>();
        for (Post post : postList) {
            parentPostSet.add(post.getParentPostId());
        }
        //判断是否在同一级机构下
        if (parentPostSet.size() > 1) {
            ExceptionCast.cast(CommonCode.NOT_SAVE_LEVEL_EXCEPTION);
        }
        postDao.sortPorts(postIds);
    }

    @Transactional
    @Override
    public void copyPost(List<Integer> postIds, UserSession userSession, Integer orgId) {
        if (!CollectionUtils.isEmpty(postIds)) {
            for (Integer postId : postIds) {
                Post post = postDao.selectByPrimaryKey(postId);
                post.setOrgId(orgId);
                post.setParentPostId(0);
                post.setOperatorId(userSession.getArchiveId());
                generatePostCodeAndSortId(post, orgId, null);
                postDao.insertSelective(post);
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

    @Override
    public List<Post> getAllPost(UserSession userSession, Integer orgId, Short isEnable) {
        //递归拿到所有子机构id
        //TODO id重复无影响
        List<Integer> orgidList = getOrgIdList(userSession, orgId);
        List<Post> postList = postDao.getPostPositionListByOrgIds(orgidList);
        return postList;
    }

    /**
     * 搜集机构下所有子机构的id
     *
     * @param userSession
     * @param orgId
     * @return
     */
    private List<Integer> getOrgIdList(UserSession userSession, Integer orgId) {
        List<Integer> idsList = new ArrayList<>();
        //先查询到所有机构
        List<OrganizationVO> allOrgs = organizationDao.getAllOrganizationByArchiveId(userSession.getArchiveId(), Short.parseShort("1"), new Date());
        //将机构的id和父id存入MultiMap,父id作为key，子id作为value，一对多
        MultiValuedMap<Integer, Integer> multiValuedMap = new HashSetValuedHashMap<>();
        for (OrganizationVO org : allOrgs) {
            multiValuedMap.put(org.getOrgParentId(), org.getOrgId());
        }
        //根据机构id递归，取出该机构下的所有子机构
        collectOrgIds(multiValuedMap, orgId, idsList);
        return idsList;
    }

    /**
     * 遍历搜集机构下所有子机构的id
     *
     * @param multiValuedMap
     * @param orgId
     * @param idsList
     */
    private void collectOrgIds(MultiValuedMap<Integer, Integer> multiValuedMap, Integer orgId, List<Integer> idsList) {
        idsList.add(orgId);
        Collection<Integer> sonOrgIds = multiValuedMap.get(orgId);
        for (Integer sonOrgId : sonOrgIds) {
            idsList.add(sonOrgId);
            if (multiValuedMap.get(sonOrgId).size() > 0) {
                collectOrgIds(multiValuedMap, sonOrgId, idsList);
            }
        }
    }


    @Override
    public List<UserArchivePostRelation> getPostSuccessive(Integer postId) {

        return postDao.getPostSuccessive(postId);
    }

    @Override
    public List<Post> exportPost(Integer orgId, List<Integer> postIds, UserSession userSession) {
        if (CollectionUtils.isEmpty(postIds)) {
            List<Integer> orgIdList = getOrgIdList(userSession, orgId);
            return postDao.getPostPositionListByOrgIds(orgIdList);
        } else {
            return postDao.getPostListByPostIds(postIds);
        }
    }

    @Override
    @Transactional
    public ResponseResult uploadAndCheck(MultipartFile multfile, UserSession userSession, HttpServletResponse response) throws Exception {
        ResponseResult responseResult = new ResponseResult(CommonCode.FAIL);
        //将校验结果与原表格信息返回
        HashMap<Object, Object> resultMap = new HashMap<>();
        //判断文件名
        String filename = multfile.getOriginalFilename();
        if (!(filename.endsWith(".xls") || filename.endsWith(".xlsx"))) {
            responseResult.setResultCode(CommonCode.FILE_FORMAT_ERROR);
            return responseResult;
        }
        List<Object> objects = ExcelImportUtil.importExcel(multfile.getInputStream(), Post.class);
        List<Post> orgList = new ArrayList<>();
        //记录行号
        Map<String, Integer> lineNumber = new HashMap<>();
        Integer number = 1;
        for (Object object : objects) {
            Post vo = (Post) object;
            lineNumber.put(vo.getPostCode(), number++);
            orgList.add(vo);
        }
        orgList.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Post post1 = (Post) o1;
                Post post2 = (Post) o2;
                if (post1.getPostCode().length() > post1.getPostCode().length()) {
                    return 1;
                } else if (post1.getPostCode().length() < post1.getPostCode().length()) {
                    return -1;
                }
                return Long.compare(Long.parseLong(post1.getPostCode()), Long.parseLong(post2.getPostCode()));
            }
        });
        //校验
        List<Post> checkResultList = checkExcel(lineNumber, orgList, userSession);
        //拿到错误校验列表
        List<Post> failCheckList = checkResultList.stream().filter(check -> {
            if (!check.getCheckResult()) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        //如果为空则校验成功
        if (CollectionUtils.isEmpty(failCheckList)) {
            responseResult.setMessage("文件校验成功");
            responseResult.setResultCode(CommonCode.SUCCESS);
        } else {
            StringBuilder errorSb = new StringBuilder();
            for (Post error : failCheckList) {
                errorSb.append(error.getLineNumber() + "," + error.getResultMsg() + "\n");
            }
            String errorInfoKey = "errorPostData" + String.valueOf(filename.hashCode());
            redisService.del(errorInfoKey);
            redisService.setex(errorInfoKey, 60 * 60 * 2, errorSb.toString());
            resultMap.put("errorInfoKey", errorInfoKey);
            responseResult.setResultCode(CommonCode.FILE_PARSING_EXCEPTION);
            response.setHeader("errorInfoKey", errorInfoKey);
        }
        //将orgList存入redis
        String redisKey = "tempPostData" + String.valueOf(filename.hashCode());
        redisService.del(redisKey);
        String json = JSON.toJSONString(orgList);
        redisService.setex(redisKey, 60 * 60 * 2, json);

        resultMap.put("failCheckList", failCheckList);
        resultMap.put("excelList", orgList);
        resultMap.put("redisKey", redisKey);
        responseResult.setResult(resultMap);
        response.setHeader("redisKey", redisKey);
        return responseResult;
    }

    @Override
    @Transactional
    public void importToDatabase(String redisKey, UserSession userSession) {
        String data = redisService.get(redisKey.trim());
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
                    return Long.compare(Long.parseLong(post1.getPostCode()), Long.parseLong(post2.getPostCode()));
                }
            });
            //TODO
            int sortId = 1000;
            for (Post vo : orgLost) {
                Post ifExistVo = postDao.getPostByPostCode(vo.getPostCode(), userSession.getCompanyId());
                Post parentPost = postDao.getPostByPostCode(vo.getParentPostCode(), userSession.getCompanyId());
                OrganizationVO orgVo = organizationDao.getOrganizationByOrgCodeAndCompanyId(vo.getOrgCode(), userSession.getCompanyId());
                Position position = positionDao.getPositionByNameAndCompanyId(vo.getPositionName(),userSession.getCompanyId());
                vo.setOrgId(orgVo.getOrgId());
                vo.setPositionId(position.getPositionId());

                if (Objects.nonNull(parentPost)) {
                    vo.setParentPostId(parentPost.getPostId());
                } else {
                    vo.setParentPostId(0);
                }
                //已存在 则更新
                if (Objects.nonNull(ifExistVo)) {
                    postDao.updateByPrimaryKeySelective(vo);
                } else {
                    vo.setOperatorId(userSession.getArchiveId());
                    vo.setCompanyId(userSession.getCompanyId());
                    vo.setSortId(sortId);
                    sortId += 1000;
                    int i = postDao.insertSelective(vo);
                }
            }
        }
    }

    @Override
    public List<Post> getPostGraphics(UserSession userSession, Integer layer, boolean isContainsCompiler, boolean isContainsActualMembers, Integer postId, Short isEnable) {
        List<Integer> orgidList = new ArrayList<>();
        //拿到关联的所有机构id
        List<Integer> postIdList = null;
        if (layer < 1) {
            layer = 2;
        }
        postIdList = getPostIdList(userSession, postId, (layer - 1), isEnable);
        //查询所有相关的岗位
        List<Post> allPost = postDao.getPostGraphics(postIdList, isEnable);
        if(CollectionUtils.isEmpty(allPost)){
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
        Post parentPost = postDao.selectByPrimaryKey(allPost.get(0).getParentPostId());

        if(Objects.nonNull(parentPost)){
            parentPost.setChildList(allPost);
            List<Post> pList=new ArrayList<>();
            pList.add(parentPost);
            Post parentPost2 = postDao.selectByPrimaryKey(parentPost.getParentPostId());
            if(Objects.nonNull(parentPost2)){
                parentPost2.setChildList(pList);
                List<Post> pList2=new ArrayList<>();
                pList2.add(parentPost2);
                return pList2;
            }
            return pList;
        }
        return allPost;
    }

    @Override
    public PageResult<Post> listDirectPostPage(PostPageVo postPageVo) {

        String sortFieldStr = "";
        if (!CollectionUtils.isEmpty(postPageVo.getQuerFieldVos())) {
            Optional<List<QueryField>> querFieldVos = Optional.of(postPageVo.getQuerFieldVos());
            sortFieldStr = QueryFieldUtil.getSortFieldStr(querFieldVos, Organization.class);
        }
        if (postPageVo.getCurrentPage() != null && postPageVo.getPageSize() != null) {
            PageHelper.startPage(postPageVo.getCurrentPage(), postPageVo.getPageSize());
        }
        List<Post> postList = postDao.listDirectPostPage(postPageVo, sortFieldStr);
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

    private List<Integer> getPostIdList(UserSession userSession, Integer postId, int layer, Short isEnable) {
        List<Integer> idsList = new ArrayList<>();
        //先查询到岗位
        List<Post> listPost = postDao.listPostByCompanyId(userSession.getCompanyId(), isEnable);
        //将机构的id和父id存入MultiMap,父id作为key，子id作为value，一对多
        MultiValuedMap<Integer, Integer> multiValuedMap = new HashSetValuedHashMap<>();
        for (Post post : listPost) {
            multiValuedMap.put(post.getParentPostId(), post.getPostId());
        }
        //根据机构id递归，取出该机构下的所有子机构
        collectOrgIds(multiValuedMap, postId, idsList, layer);
        return idsList;

    }

    private void collectOrgIds(MultiValuedMap<Integer, Integer> multiValuedMap, Integer postId, List<Integer> idsList, Integer layer) {
        idsList.add(postId);
        Collection<Integer> sonPostIds = multiValuedMap.get(postId);
        for (Integer sonPostId : sonPostIds) {

            if (layer != null && layer > 0) {
                idsList.add(sonPostId);
                if (multiValuedMap.get(sonPostId).size() > 0 && layer > 0) {
                    layer--;
                    collectOrgIds(multiValuedMap, sonPostId, idsList, layer);

                }
            } else {
                idsList.add(sonPostId);

            }
        }
    }

    /**
     * 删除修改不含有的岗位职等关系信息
     * @param postVo
     * @param userSession
     * @param post
     */
    /*private void deletePostGrade(PostVo postVo, UserSession userSession, Post post) {
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
*/

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
     * 删除修改不含有的岗位职级关系信息
     * @param postVo
     * @param userSession
     * @param post
     */
   /* private void deletePostLevel(PostVo postVo, UserSession userSession, Post post) {
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
*/

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



   /* private void addPostLevelAndGradeRelation(PostVo postVo, UserSession userSession, Post post) {
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
*/

     /*   //新增岗位职等关系表信息
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
    }*/


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


    public List<Post> checkExcel(Map<String, Integer> lineNumber, List<Post> voList, UserSession userSession) {
        List<Post> checkVos = new ArrayList<>();
        int line = 1;
        int groupCount = 0;
        for (Post post : voList) {
            Post checkVo = new Post();
            BeanUtils.copyProperties(post, checkVo);
            checkVo.setLineNumber(line++);
            checkVo.setCheckResult(true);
            StringBuilder resultMsg = new StringBuilder();
            //验空
            if (Objects.isNull(post.getPostCode())) {
                checkVo.setCheckResult(false);
                resultMsg.append("岗位编码不能为空|");
            }
            if (Objects.isNull(post.getPostName())) {
                checkVo.setCheckResult(false);
                resultMsg.append("岗位名称不能为空|");
            }
            if (Objects.isNull(post.getOrgCode())) {
                checkVo.setCheckResult(false);
                resultMsg.append("所属部门编码不能为空|");
            }
            if (Objects.isNull(post.getOrgName())) {
                checkVo.setCheckResult(false);
                resultMsg.append("所属部门名称不能为空|");
            }
            if (Objects.isNull(post.getPositionName())) {
                checkVo.setCheckResult(false);
                resultMsg.append("职位名称不能为空|");
            }

            // 校验部门编码是否存在，部门编码与部门名称是否对应
            OrganizationVO org = organizationDao.getOrganizationByOrgCodeAndCompanyId(post.getOrgCode(), userSession.getCompanyId());
            if (Objects.isNull(org)) {
                checkVo.setCheckResult(false);
                resultMsg.append("部门编码" + org.getOrgCode() + "不存在|");
            } else {
                if (!post.getOrgName().equals(org.getOrgName())) {
                    checkVo.setCheckResult(false);
                    resultMsg.append("部门名称不匹配|");
                }
            }

            if (null != post.getParentPostCode() && "".equals(post.getParentPostCode())) {
                // 校验上级岗位编码是否存在，与岗位名称是否对应
                Post parentPost = postDao.getPostByPostCode(post.getParentPostCode(), userSession.getCompanyId());
                if (Objects.nonNull(parentPost)) {
                    if (!post.getParentPostName().equals(parentPost.getPostName())) {
                        checkVo.setCheckResult(false);
                        resultMsg.append("上级岗位名称不匹配|");
                    }
                }
            }

            //校验职位是否存在
            Position position = positionDao.getPositionByNameAndCompanyId(post.getPositionName(),userSession.getCompanyId());
            if (Objects.isNull(position)) {
                checkVo.setCheckResult(false);
                resultMsg.append("职位" + post.getPositionName() + "不存在|");
            }
            checkVo.setResultMsg(resultMsg);
            checkVos.add(checkVo);
        }
        return checkVos;
    }


}
