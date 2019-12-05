package com.qinjee.masterdata.controller.organization;

import com.qinjee.exception.BusinessException;
import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.Post;
import com.qinjee.masterdata.model.entity.UserArchivePostRelation;
import com.qinjee.masterdata.model.vo.organization.PostVo;
import com.qinjee.masterdata.model.vo.organization.page.PostPageVo;
import com.qinjee.masterdata.service.organation.PostService;
import com.qinjee.masterdata.utils.pexcel.ExcelExportUtil;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月11日 15:17:00
 */
@Api(tags = "【机构管理】岗位接口")
@RestController
@RequestMapping("/post")
public class PostController extends BaseController {

    @Autowired
    private PostService postService;

    private final static String xls = "xls";
    private final static String xlsx = "xlsx";


    private Boolean checkParam(Object... params) {
        for (Object param : params) {
            if (null == param || "".equals(param)) {
                return false;
            }
        }
        return true;
    }

    @PostMapping("/getPostList")
    @ApiOperation(value = "ok，分页查询岗位列表,orgId（必填）、postId（选填）", notes = "ok")
    public ResponseResult<PageResult<Post>> getPostList(@RequestBody PostPageVo postPageVo) {
        if (checkParam(postPageVo)) {
            try {

                PageResult<Post> pageResult = postService.getPostConditionPage(getUserSession(), postPageVo);
                return new ResponseResult<>(pageResult);
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof BusinessException) {
                    BusinessException be = (BusinessException) e;
                    return new ResponseResult<>(null, be.getResultCode());
                }
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "isEnable", value = "是否包含封存：0 封存、1 解封（默认）", paramType = "query", dataType = "short")
    })
    @GetMapping("/getAllPost")
    @ApiOperation(value = "ok，根据机构id获取机构下（包含子机构）所有的岗位", notes = "ok")
    public ResponseResult<List<Post>> getAllPost(@RequestParam("orgId") @ApiParam(name = "orgId", value = "机构id", example = "1", required = true) Integer orgId,
                                                 @RequestParam("isEnable") @ApiParam(name = "isEnable", value = "是否包含封存的岗位", example = "1", required = true) Short isEnable) {
        if (checkParam(orgId, isEnable)) {
            try {
                List<Post> posts = postService.getAllPostByOrgId(getUserSession(), orgId, isEnable);
                return new ResponseResult<>(posts);
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof BusinessException) {
                    BusinessException be = (BusinessException) e;
                    return new ResponseResult<>(null, be.getResultCode());
                }
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }


    @PostMapping("/getDirectPostPageList")
    @ApiOperation(value = "ok，分页查询下级直属机构(id可以是机构orgId、parentPostId)", notes = "ok")
    public ResponseResult<PageResult<Post>> getDirectPostPageList(@RequestBody PostPageVo postPageVo) {
        if (checkParam(postPageVo)) {
            try {
                Short isEnable = postPageVo.getIsEnable();
                if (isEnable == null || isEnable == 0) {
                    isEnable = 1;
                } else {
                    isEnable = null;
                }
                postPageVo.setIsEnable(isEnable);
                PageResult<Post> pageResult = postService.listDirectPostPage(postPageVo);
                return new ResponseResult<>(pageResult);
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof BusinessException) {
                    BusinessException be = (BusinessException) e;
                    return new ResponseResult<>(null, be.getResultCode());
                }
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }


    @ApiOperation(value = "ok，新增岗位", notes = "ok")
    @PostMapping("/addPost")
    public ResponseResult addPost(@RequestBody PostVo postVo) {
        if (checkParam(postVo)) {
            try {
                postService.addPost(postVo, getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof BusinessException) {
                    BusinessException be = (BusinessException) e;
                    return new ResponseResult<>(null, be.getResultCode());
                }
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @ApiOperation(value = "ok，编辑岗位", notes = "ok")
    @PostMapping("/editPost")
    public ResponseResult editPost(PostVo postVo) {
        if (checkParam(postVo)) {
            try {
                postService.editPost(postVo, getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof BusinessException) {
                    BusinessException be = (BusinessException) e;
                    return new ResponseResult<>(null, be.getResultCode());
                }
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @ApiOperation(value = "ok，删除岗位", notes = "ok，")
    @PostMapping("/deletePost")
    public ResponseResult deletePost(@RequestBody List<Integer> postIds) {
        if (checkParam(postIds)) {
            try {
                postService.deletePost(getUserSession(), postIds);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof BusinessException) {
                    BusinessException be = (BusinessException) e;
                    return new ResponseResult<>(null, be.getResultCode());
                }
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @PostMapping("/lockPostByIds")
    @ApiOperation(value = "ok，封存岗位", notes = "ok")
    public ResponseResult lockPostByIds(@RequestBody List<Integer> postIds) {
        if (checkParam(postIds)) {
            try {
                postService.sealPostByIds(postIds, Short.parseShort("0"), getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof BusinessException) {
                    BusinessException be = (BusinessException) e;
                    return new ResponseResult<>(null, be.getResultCode());
                }
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @PostMapping("/unlockPostByIds")
    @ApiOperation(value = "ok，解封岗位", notes = "ok")
    public ResponseResult unlockPostByIds(@RequestBody List<Integer> postIds) {
        if (checkParam(postIds)) {
            try {
                postService.sealPostByIds(postIds, Short.parseShort("1"), getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof BusinessException) {
                    BusinessException be = (BusinessException) e;
                    return new ResponseResult<>(null, be.getResultCode());
                }
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }


    @PostMapping("/sortPorts")
    @ApiOperation(value = "ok，岗位排序,只能同一级别下排序（需要将该级下所有岗位id按顺序传参）", notes = "未验证")
    public ResponseResult sortPorts(@RequestBody LinkedList<Integer> postIds) {
        if (checkParam(postIds)) {
            try {
                postService.sortPorts(postIds, getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof BusinessException) {
                    BusinessException be = (BusinessException) e;
                    return new ResponseResult<>(null, be.getResultCode());
                }
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @ApiOperation(value = "ok,查看岗位历任")
    @GetMapping("/getPostSuccessive")
    public ResponseResult<List<UserArchivePostRelation>> getPostSuccessive(@RequestParam("postId") Integer postId) {
        if (checkParam(postId)) {
            try {
                List<UserArchivePostRelation> users = postService.getPostSuccessive(postId);
                return new ResponseResult(users);
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof BusinessException) {
                    BusinessException be = (BusinessException) e;
                    return new ResponseResult<>(null, be.getResultCode());
                }
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }


    @ApiOperation(value = "ok，复制岗位,将岗位复制到机构下  参数demo {\"orgId\":1,\"postIds\":[41,11]}", notes = "未验证")
    @PostMapping("/copyPost")
    public ResponseResult copyPost(@RequestBody Map<String, Object> paramMap) {
        if (checkParam(paramMap)) {
            try {
                List<Integer> postIds = (List<Integer>) paramMap.get("postIds");
                Integer orgId = (Integer) paramMap.get("orgId");
                if (checkParam(postIds, orgId)) {
                    postService.copyPost(postIds, getUserSession(), orgId);
                    return ResponseResult.SUCCESS();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof BusinessException) {
                    BusinessException be = (BusinessException) e;
                    return new ResponseResult<>(null, be.getResultCode());
                }
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);

    }

    // String filePath,  List<Integer> orgIds
    @PostMapping("/exportPost")
    @ApiOperation(value = "ok，导出岗位  参数demo {\"orgId\":28,\"postIds\":[1,47]}")
    public ResponseResult exportPost(@RequestBody Map<String, Object> paramMap, HttpServletResponse response) throws Exception {
        ResponseResult responseResult = new ResponseResult();
        List<Integer> postIds = null;
        Integer orgId = null;
        if (paramMap.get("postIds") != null && paramMap.get("postIds") instanceof List) {
            postIds = (List<Integer>) paramMap.get("postIds");
        }
        if (paramMap.get("orgId") != null && paramMap.get("orgId") instanceof Integer) {
            orgId = (Integer) paramMap.get("orgId");
        }
        try {
            List<Post> postOList = postService.exportPost(orgId, postIds, getUserSession());
            if (!CollectionUtils.isEmpty(postOList)) {
                byte[] bytes = ExcelExportUtil.exportToBytes(postOList);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("content-Type", "application/vnd.ms-excel");
                response.setHeader("fileName", URLEncoder.encode("postDefualt.xls", "UTF-8"));
                response.setHeader("Content-Disposition",
                    "attachment;filename=\"" + URLEncoder.encode("postDefualt", "UTF-8") + "\"");
                response.getOutputStream().write(bytes);
                return null;
            } else {
                responseResult.setMessage("岗位为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setResultCode(CommonCode.FILE_EXPORT_FAILED);
        }

        return responseResult;
    }

    @PostMapping("/uploadAndCheck")
    @ApiOperation(value = "ok，导入岗位excel并校验，校验成功后存入redis并返回key，校验错误则返回错误信息列表", notes = "ok")
    public ResponseResult uploadAndCheck(MultipartFile multfile, HttpServletResponse response) throws Exception {
        if (checkParam(multfile)) {
            try {
                return postService.uploadAndCheck(multfile, getUserSession(), response);
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof BusinessException) {
                    BusinessException be = (BusinessException) e;
                    return new ResponseResult<>(null, be.getResultCode());
                }
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);

    }

    @GetMapping("/exportError2Txt")
    @ApiOperation(value = "ok,导出错误信息到txt", notes = "ok")
    public ResponseResult exportError2Txt(String errorInfoKey, HttpServletResponse response) throws Exception {
        if (checkParam(errorInfoKey)) {
            try {
                String errorData = redisClusterService.get(errorInfoKey.trim());
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/x-msdownload;charset=UTF-8");
                response.setHeader("Content-Disposition",
                    "attachment;filename=\"" + URLEncoder.encode("errorInfo.txt", "UTF-8") + "\"");
                response.setHeader("fileName", URLEncoder.encode("errorInfo.txt", "UTF-8"));
                response.getOutputStream().write(errorData.getBytes());
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof BusinessException) {
                    BusinessException be = (BusinessException) e;
                    return new ResponseResult<>(null, be.getResultCode());
                }
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }


    @GetMapping("/importToDatabase")
    @ApiOperation(value = "ok,导入岗位入库")
    public ResponseResult importToDatabase(@RequestParam("redisKey") String redisKey) {
        if (checkParam(redisKey)) {
            try {
                postService.importToDatabase(redisKey, getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof BusinessException) {
                    BusinessException be = (BusinessException) e;
                    return new ResponseResult<>(null, be.getResultCode());
                }
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @GetMapping("/cancelImport")
    @ApiOperation(value = "ok,取消导入(将数据从redis中删除)")
    public ResponseResult cancelImport(@RequestParam("redisKey") String redisKey, @RequestParam("errorInfoKey") String errorInfoKey) {
        if (checkParam(redisKey)) {
            try {
                postService.cancelImport(redisKey.trim(), errorInfoKey.trim());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof BusinessException) {
                    BusinessException be = (BusinessException) e;
                    return new ResponseResult<>(null, be.getResultCode());
                }
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
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

        if (checkParam(layer,isContainsCompiler,isContainsActualMembers,postId,isEnable)) {
            try {
                if (isEnable != 0) {
                    isEnable = null;
                }
                List<Post> pageResult = postService.getPostGraphics(getUserSession(), layer, isContainsCompiler, isContainsActualMembers, postId, isEnable);
                return new ResponseResult(pageResult);
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof BusinessException) {
                    BusinessException be = (BusinessException) e;
                    return new ResponseResult<>(null, be.getResultCode());
                }
                return new ResponseResult<>(null, CommonCode.BUSINESS_EXCEPTION);
            }
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }
}
