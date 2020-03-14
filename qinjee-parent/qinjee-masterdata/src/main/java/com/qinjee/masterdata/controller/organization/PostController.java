package com.qinjee.masterdata.controller.organization;

import com.github.liaochong.myexcel.core.DefaultExcelBuilder;
import com.github.liaochong.myexcel.utils.AttachmentExportUtil;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.Post;
import com.qinjee.masterdata.model.entity.UserArchivePostRelation;
import com.qinjee.masterdata.model.vo.organization.PostVo;
import com.qinjee.masterdata.model.vo.organization.bo.PostBO;
import com.qinjee.masterdata.model.vo.organization.bo.PostCopyBO;
import com.qinjee.masterdata.model.vo.organization.bo.PostExportBO;
import com.qinjee.masterdata.model.vo.organization.bo.PostPageBO;
import com.qinjee.masterdata.service.organation.PostService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

@Api(tags = "【机构管理】岗位接口")
@RestController
@RequestMapping("/post")
public class PostController extends BaseController {
    private static Logger logger = LogManager.getLogger(PostController.class);
    @Autowired
    private PostService postService;

    private final static String xls = "xls";
    private final static String xlsx = "xlsx";


    private Boolean checkParam(Object... params) {
        for (Object param : params) {
            if (param instanceof UserSession) {
                if (null == param || "".equals(param)) {
                    ExceptionCast.cast(CommonCode.INVALID_SESSION);
                    return false;
                }
            }
            if (null == param || "".equals(param)) {
                return false;
            }
        }
        return true;
    }

    @PostMapping("/getPostList")
    @ApiOperation(value = "ok，分页查询岗位列表,orgId（必填）、postId（选填）", notes = "ok")
    public ResponseResult<PageResult<Post>> getPostList(@RequestBody PostPageBO postPageBO) {
        if (checkParam(postPageBO, getUserSession())) {
            if (null!=postPageBO.getIsEnable()&&postPageBO.getIsEnable() != 0) {
                postPageBO.setIsEnable(null);
            }
            long start = System.currentTimeMillis();
            PageResult<Post> pageResult = postService.getPostList(getUserSession(), postPageBO);
            logger.info("分页查询岗位列表耗时:" + (System.currentTimeMillis() - start));
            return new ResponseResult<>(pageResult);
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @GetMapping("/getPostById")
    @ApiOperation(value = "ok，根据岗位id查询", notes = "ok")
    public ResponseResult<Post> getPostById(String postId) {
        if (checkParam(postId)) {
            long start = System.currentTimeMillis();
            Post post = postService.getPostById(postId);
            logger.info("根据岗位id查询耗时:" + (System.currentTimeMillis() - start));
            return new ResponseResult<>(post);
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }





    @PostMapping("/getDirectPostPageList")
    @ApiOperation(value = "ok，查询机构下的直属岗位（包含封存，用于排序）", notes = "ok")
    public ResponseResult<PageResult<Post>> getDirectPostPageList(@RequestBody PostBO postBO) {
        if (checkParam(postBO, getUserSession())) {
            long start = System.currentTimeMillis();
            PageResult<Post> pageResult = postService.listDirectPostPage(postBO);
            logger.info("查询下级直属岗位耗时:" + (System.currentTimeMillis() - start));
            return new ResponseResult<>(pageResult);
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }


    @ApiOperation(value = "ok，新增岗位", notes = "ok")
    @PostMapping("/addPost")
    public ResponseResult addPost(@RequestBody PostVo postVo) {
        if (checkParam(postVo, getUserSession())) {
            long start = System.currentTimeMillis();
            postService.addPost(postVo, getUserSession());
            logger.info("新增岗位耗时:" + (System.currentTimeMillis() - start));
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @ApiOperation(value = "ok，编辑岗位", notes = "ok")
    @PostMapping("/editPost")
    public ResponseResult editPost(@RequestBody  PostVo postVo) {
        if (checkParam(postVo, getUserSession())) {
            long start = System.currentTimeMillis();
            postService.editPost(postVo, getUserSession());
            logger.info("编辑岗位耗时:" + (System.currentTimeMillis() - start));
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @ApiOperation(value = "ok，删除岗位", notes = "ok，")
    @PostMapping("/deletePost")
    public ResponseResult deletePost(@RequestBody List<Integer> postIds) {
        if (checkParam(postIds, getUserSession())) {
            long start = System.currentTimeMillis();
            postService.deletePost(getUserSession(), postIds);
            logger.info("删除岗位耗时:" + (System.currentTimeMillis() - start));
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @PostMapping("/lockPostByIds")
    @ApiOperation(value = "ok，封存岗位", notes = "ok")
    public ResponseResult lockPostByIds(@RequestBody List<Integer> postIds) {
        if (checkParam(postIds, getUserSession())) {
            long start = System.currentTimeMillis();
            postService.sealPostByIds(postIds, Short.parseShort("0"), getUserSession());
            logger.info("封存岗位耗时:" + (System.currentTimeMillis() - start));
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @PostMapping("/unlockPostByIds")
    @ApiOperation(value = "ok，解封岗位", notes = "ok")
    public ResponseResult unlockPostByIds(@RequestBody List<Integer> postIds) {
        if (checkParam(postIds, getUserSession())) {
            long start = System.currentTimeMillis();
            postService.sealPostByIds(postIds, Short.parseShort("1"), getUserSession());
            logger.info("解封岗位耗时:" + (System.currentTimeMillis() - start));
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }


    @PostMapping("/sortPorts")
    @ApiOperation(value = "ok，岗位排序,在同机构下 排序，不需要同岗位级别", notes = "未验证")
    public ResponseResult sortPorts(@RequestBody List<Integer> postIds) {
        if (checkParam(postIds, getUserSession())) {
            long start = System.currentTimeMillis();
            postService.sortPorts(postIds, getUserSession());
            logger.info("排序岗位耗时:" + (System.currentTimeMillis() - start));
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @ApiOperation(value = "ok,查看岗位历任")
    @GetMapping("/getPostSuccessive")
    public ResponseResult<List<UserArchivePostRelation>> getPostSuccessive(@RequestParam("postId") Integer postId) {
        if (checkParam(postId, getUserSession())) {
            long start = System.currentTimeMillis();
            List<UserArchivePostRelation> users = postService.getPostSuccessive(postId);
            logger.info("查看岗位历任耗时:" + (System.currentTimeMillis() - start));
            return new ResponseResult(users);
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }


    @ApiOperation(value = "ok，复制岗位,将岗位复制到机构下  ", notes = "phs")
    @PostMapping("/copyPost")
    public ResponseResult copyPost(@RequestBody PostCopyBO postCopyBO) {
        if (checkParam(postCopyBO, getUserSession())) {
            long start = System.currentTimeMillis();
                postService.copyPost(postCopyBO, getUserSession());
                logger.info("复制岗位耗时:" + (System.currentTimeMillis() - start));
                return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    // String filePath,  List<Integer> orgIds
    @PostMapping("/exportPost")
    @ApiOperation(value = "ok，导出岗位 ")
    public ResponseResult exportPost(@RequestBody PostExportBO postExportBO, HttpServletResponse response) throws Exception {
        ResponseResult responseResult = new ResponseResult();
        long start = System.currentTimeMillis();
        Boolean b = checkParam(postExportBO, getUserSession());
        if(b){

            List<Post> postList = postService.exportPost(postExportBO, getUserSession());
            if (!CollectionUtils.isEmpty(postList)) {
                //response.setHeader("fileName", URLEncoder.encode("岗位信息.xls", "UTF-8"));
                Workbook workbook = DefaultExcelBuilder.of(Post.class).build(postList);
                AttachmentExportUtil.export(workbook, "岗位信息", response);
                //只能返回null
                logger.info("导出岗位耗时：" + (System.currentTimeMillis() - start) + "ms");
                return null;
            } else {
                responseResult.setMessage("岗位为空");
            }
            return responseResult;
        }else{
            return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
        }
    }

    @PostMapping("/uploadAndCheck")
    @ApiOperation(value = "ok，导入岗位excel并校验，校验成功后存入redis并返回key，校验错误则返回错误信息列表", notes = "ok")
    public ResponseResult uploadAndCheck(MultipartFile multfile) throws Exception {
        if (checkParam(multfile, getUserSession())) {
            logger.info("导入岗位excel并校验");
            long start = System.currentTimeMillis();
            ResponseResult responseResult = postService.uploadAndCheck(multfile, getUserSession());
            logger.info("导入岗位excel并校验耗时：" + (System.currentTimeMillis() - start) + "ms");
            return responseResult;
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);

    }

    @GetMapping("/exportError2Txt")
    @ApiOperation(value = "ok,导出错误信息到txt", notes = "ok")
    public ResponseResult exportError2Txt(String errorInfoKey, HttpServletResponse response) throws Exception {
        if (checkParam(errorInfoKey, getUserSession())) {
            long start = System.currentTimeMillis();
            String errorData = redisClusterService.get(errorInfoKey.trim());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-msdownload;charset=UTF-8");
            response.setHeader("Content-Disposition",
                "attachment;filename=\"" + URLEncoder.encode("岗位导入错误校验信息.txt", "UTF-8") + "\"");
            response.setHeader("fileName", URLEncoder.encode("岗位导入错误校验信息.txt", "UTF-8"));
            response.getOutputStream().write(errorData.getBytes());
            logger.info("导出错误信息到txt耗时：" + (System.currentTimeMillis() - start) + "ms");
            return null;
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }


    @GetMapping("/importToDatabase")
    @ApiOperation(value = "ok,导入岗位入库")
    public ResponseResult importToDatabase(@RequestParam("redisKey") String redisKey) {
        if (checkParam(redisKey, getUserSession())) {
            long start = System.currentTimeMillis();
            postService.importToDatabase(redisKey, getUserSession());
            logger.info("导出错误信息到txt耗时：" + (System.currentTimeMillis() - start) + "ms");
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @GetMapping("/cancelImport")
    @ApiOperation(value = "ok,取消导入(将数据从redis中删除)")
    public ResponseResult cancelImport(@RequestParam("redisKey") String redisKey, @RequestParam("errorInfoKey") String errorInfoKey) {
        if (checkParam(redisKey, getUserSession())) {
            postService.cancelImport(redisKey.trim(), errorInfoKey.trim());
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }


    //TODO 实有人数、编制人数暂时不考虑
    //TODO 递归层数控制
    @ApiOperation(value = "ok，获取岗位图", notes = "ok")
    @GetMapping("/getPostGraphics")
    public ResponseResult<List<Post>> getPostGraphics(@RequestParam("layer") @ApiParam(value = "岗位图层数，默认显示2级", example = "2") Integer layer,
                                                      @RequestParam("isContainsCompiler") @ApiParam(value = "是否显示编制人数", example = "false") boolean isContainsCompiler,
                                                      @RequestParam("isContainsActualMembers") @ApiParam(value = "是否显示实有人数", example = "false") boolean isContainsActualMembers,
                                                      @RequestParam("postId") @ApiParam(value = "postId", example = "1") Integer postId,
                                                      @RequestParam("isEnable") @ApiParam(value = "是否包含封存：0不包含（默认）、1 包含", example = "0") Short isEnable) {

        if (checkParam(layer, isContainsCompiler, isContainsActualMembers, postId, isEnable, getUserSession())) {
            if (isEnable != 0) {
                isEnable = null;
            }
            long start = System.currentTimeMillis();
            List<Post> pageResult = postService.getPostGraphics(getUserSession(), layer, isContainsCompiler, isContainsActualMembers, postId, isEnable);
            logger.info("获取岗位图耗时:" + (System.currentTimeMillis() - start));
            return new ResponseResult(pageResult);
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @ApiOperation(value = "根据postId生成岗位编码（用于新增机构时获取新岗位编码）")
    @GetMapping("/generatePostCode")
    public ResponseResult generatePostCode(@RequestParam("orgId") Integer orgId, @RequestParam(value = "parentPostId", required = false) Integer parentPostId) {
        //校验参数
        if (checkParam(orgId, getUserSession())) {
            long start = System.currentTimeMillis();
            String postCode = postService.generatePostCode(orgId, parentPostId);
            logger.info("根据postId生成岗位编码耗时:" + (System.currentTimeMillis() - start));
            return new ResponseResult<>(postCode);
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

}
