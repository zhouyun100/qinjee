package com.qinjee.masterdata.service.organation.impl;

import com.github.pagehelper.PageHelper;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.PostDao;
import com.qinjee.masterdata.dao.PostGradeRelationDao;
import com.qinjee.masterdata.dao.PostInstructionsDao;
import com.qinjee.masterdata.dao.PostLevelRelationDao;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.dao.staffdao.userarchivedao.UserArchivePostRelationDao;
import com.qinjee.masterdata.model.entity.*;
import com.qinjee.masterdata.model.vo.organization.PostPageVo;
import com.qinjee.masterdata.model.vo.organization.PostVo;
import com.qinjee.masterdata.model.vo.organization.QueryFieldVo;
import com.qinjee.masterdata.service.organation.PostService;
import com.qinjee.masterdata.utils.QueryFieldUtil;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
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
            }
        }
        return new ResponseResult();
    }

    @Override
    public ResponseResult<List<Post>> getAllPost(UserSession userSession, Integer orgId) {
        List<Post> postList = postDao.getPostPositionListByOrgId(orgId);
        return new ResponseResult<>(postList);
    }

    @Override
    public ResponseResult downloadTemplate(HttpServletResponse response) {
        ClassPathResource cpr = new ClassPathResource("/templates/"+"岗位导入模板.xls");
        try {
            File file = cpr.getFile();
            String filename = cpr.getFilename();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            FileUtils.copyFile(file,outputStream);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition",
                    "attachment;filename=\"" + URLEncoder.encode(filename, "UTF-8") + "\"");
            response.getOutputStream().write(outputStream.toByteArray());
        }catch (Exception e){
            ExceptionCast.cast(CommonCode.FILE_EXPORT_FAILED);
        }
        return new ResponseResult();
    }

    @Override
    public ResponseResult downloadExcelByCondition(PostPageVo postPageVo, UserSession userSession,HttpServletResponse response) {
        Integer archiveId = userSession.getArchiveId();
        Optional<List<QueryFieldVo>> querFieldVos = Optional.of(postPageVo.getQuerFieldVos());
        String sortFieldStr = QueryFieldUtil.getSortFieldStr(querFieldVos, Post.class);
        List<Post> postList = postDao.getPostList(postPageVo, sortFieldStr, archiveId);
        exportExcel(response,postList);
        return new ResponseResult();
    }

    @Override
    public ResponseResult downloadExcelByPostId(List<Integer> postIds, UserSession userSession, HttpServletResponse response) {
        List<Post> postList = postDao.getPostListByPostId(postIds);
        exportExcel(response,postList);
        return new ResponseResult();
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

    private static void exportExcel(HttpServletResponse response, List<Post> postList) {
        try {
            //实例化HSSFWorkbook
            HSSFWorkbook workbook = new HSSFWorkbook();
            //创建一个Excel表单，参数为sheet的名字
            HSSFSheet sheet = workbook.createSheet("sheet");
            List<String> strList = new ArrayList<>();
            strList.add("岗位编码");
            strList.add("岗位名称");
            strList.add("所属部门编码");
            strList.add("所属部门");
            strList.add("上级岗位编码");
            strList.add("上级岗位");
            strList.add("职位");
            strList.add("职级");
            strList.add("职等");
            //设置表头
            setTitle(workbook, sheet, strList);
            //设置单元格并赋值
            setData(sheet, postList);
            //设置浏览器下载
            setBrowser(response, workbook, "岗位信息.xls");
        } catch (Exception e) {
            ExceptionCast.cast(CommonCode.FILE_EXPORT_FAILED);
        }
    }

    private static void setTitle(HSSFWorkbook workbook, HSSFSheet sheet, List<String> strList) {
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位set是1/256个字符宽度
        for(int i = 0; i < strList.size(); i++){
            sheet.setColumnWidth(i, 30 * 256);
        }
        //设置为居中加粗,格式化时间格式
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);//字号
        font.setBold(true);//加粗
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);//左右居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//上下居中
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd HH:mm:ss"));
        //创建表头名称
        HSSFCell cell;
        for (int j = 0; j < strList.size(); j++) {
            cell = row.createCell(j);
            cell.setCellValue(strList.get(j));
            cell.setCellStyle(style);
        }
    }

    /**
     * 方法名：setData
     * 功能：表格赋值
     * 描述：
     * 创建人：typ
     * 创建时间：2018/10/19 16:11
     * 修改人：
     * 修改描述：
     * 修改时间：
     */
    private static void setData(HSSFSheet sheet, List<Post> postList) {
        for (int i = 0; i < postList.size(); i++) {
            Post post = postList.get(i);
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(post.getPostCode());
            row.createCell(1).setCellValue(post.getPostName());
            row.createCell(2).setCellValue(post.getParentOrgCode());
            row.createCell(3).setCellValue(post.getParentOrgName());
            row.createCell(4).setCellValue(post.getParentPostCode());
            row.createCell(5).setCellValue(post.getParentPostName());
            row.createCell(6).setCellValue(post.getPositionName());
            row.createCell(6).setCellValue(post.getPositionLevelNames());
            row.createCell(6).setCellValue(post.getPositionGradeNames());
        }
    }

    /**
     * 使用浏览器下载
     * @param response
     * @param workbook
     * @param fileName
     */
    private static void setBrowser(HttpServletResponse response, HSSFWorkbook workbook, String fileName) throws Exception {
        try {
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            //清空response
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            //将excel写入到输出流中
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            throw new Exception("文件导出失败!");
        }
    }



}
