package com.qinjee.masterdata.controller.organization;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.Post;
import com.qinjee.masterdata.model.entity.UserArchivePostRelation;
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.vo.organization.PostVo;
import com.qinjee.masterdata.model.vo.organization.page.PostPageVo;
import com.qinjee.masterdata.service.organation.OrganizationService;
import com.qinjee.masterdata.service.organation.PostService;
import com.qinjee.masterdata.utils.pexcel.ExcelExportUtil;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
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
  @Autowired
  private OrganizationService organizationService;

  @PostMapping("/getPostList")
  @ApiOperation(value = "ok，分页查询岗位列表,只有orgId字段为必须", notes = "ok")
  public ResponseResult<PageResult<Post>> getPostList(@RequestBody PostPageVo postPageVo) {
    PageResult<Post> pageResult = postService.getPostConditionPage(getUserSession(), postPageVo);
    return new ResponseResult<>(pageResult);
  }

  @ApiImplicitParams({
      @ApiImplicitParam(name = "isEnable", value = "是否包含封存：0 封存、1 解封（默认）", paramType = "query", dataType = "short")
  })
  @GetMapping("/getAllPost")
  @ApiOperation(value = "ok，根据机构id获取机构下（包含子机构）所有的岗位", notes = "ok")
  public ResponseResult<List<Post>> getAllPost(@RequestParam("orgId") @ApiParam(name = "orgId", value = "机构id", example = "1", required = true) Integer orgId,
                                               @RequestParam("isEnable") @ApiParam(name = "isEnable", value = "是否包含封存的岗位", example = "1", required = true) Short isEnable) {
    return postService.getAllPost(getUserSession(), orgId, isEnable);
  }



  @ApiOperation(value = "ok，新增岗位", notes = "ok")
  @PostMapping("/addPost")
  public ResponseResult addPost(@RequestBody PostVo postVo) {
    return postService.addPost(postVo, getUserSession());
  }

  @ApiOperation(value = "ok，编辑岗位", notes = "ok")
  @PostMapping("/editPost")
  public ResponseResult editPost(PostVo postVo) {
    return postService.editPost(postVo, getUserSession());
  }

  @ApiOperation(value = "ok，删除岗位", notes = "ok，")
  @PostMapping("/deletePost")
  public ResponseResult deletePost(@RequestBody List<Integer> postIds) {
    return postService.deletePost(getUserSession(), postIds);
  }

  @PostMapping("/lockPostByIds")
  @ApiOperation(value = "ok，封存岗位", notes = "ok")
  public ResponseResult lockPostByIds(@RequestBody List<Integer> postIds) {
    return postService.sealPostByIds(postIds, Short.parseShort("0"), getUserSession());
  }

  @PostMapping("/unlockPostByIds")
  @ApiOperation(value = "ok，解封岗位", notes = "ok")
  public ResponseResult unlockPostByIds(@RequestBody List<Integer> postIds) {
    return postService.sealPostByIds(postIds, Short.parseShort("1"), getUserSession());
  }


  @PostMapping("/sortPorts")
  @ApiOperation(value = "ok，岗位排序,只能同一级别下排序（需要将该级下所有岗位id按顺序传参）", notes = "未验证")
  public ResponseResult sortPorts(@RequestBody LinkedList<Integer> orgIds) {
    return postService.sortPorts(orgIds, getUserSession());
  }
  @ApiOperation(value = "ok,查看岗位历任")
  @GetMapping("/getPostSuccessive")
  public ResponseResult<List<UserArchivePostRelation>> getPostSuccessive(@RequestParam("postId") Integer postId) {

    return postService.getPostSuccessive(postId);
  }

  @ApiOperation(value = "未验证，下载模板", notes = "未验证")
  @GetMapping("/downloadTemplate")
  public ResponseResult downloadTemplate(HttpServletResponse response) {
    return postService.downloadTemplate(response);
  }

  @ApiOperation(value = "ok，复制岗位", notes = "未验证")
  @PostMapping("/copyPost")
  public ResponseResult copyPost(@RequestBody Map<String, Object> paramMap) {
    List<Integer> postIds = (List<Integer>) paramMap.get("postIds");
    Integer orgId = (Integer) paramMap.get("orgId");
    return postService.copyPost(postIds, getUserSession(), orgId);
  }

  // String filePath,  List<Integer> orgIds
  @PostMapping("/exportPost")
  public ResponseResult exportPost(@RequestBody Map<String, Object> paramMap, HttpServletResponse response) {
    List<Integer> postIds = null;
    Integer orgId = null;
    if (paramMap.get("postIds") != null && paramMap.get("postIds") instanceof List) {
      postIds = (List<Integer>) paramMap.get("postIds");
    }
    if (paramMap.get("orgId") != null && paramMap.get("orgId") instanceof Integer) {
      orgId = (Integer) paramMap.get("orgId");
    }
    List<Post> postOList = postService.exportPost(orgId, postIds, getUserSession());
    try {
      byte[] bytes = ExcelExportUtil.exportToBytes(postOList);
      response.setCharacterEncoding("UTF-8");
      response.setHeader("content-Type", "application/vnd.ms-excel");
      response.setHeader("fileName", URLEncoder.encode("岗位", "UTF-8"));
      response.setHeader("Content-Disposition",
              "attachment;filename=\"" + URLEncoder.encode("岗位", "UTF-8") + "\"");
      response.getOutputStream().write(bytes);
    } catch (Exception e) {

    }
    return null;
  }

  @PostMapping("/importAndCheckPostExcel")
  @ApiOperation(value = "待验证，导入岗位excel并校验，校验成功后存入redis并返回key，校验错误则返回错误信息列表", notes = "ok")
  public ResponseResult importAndCheckPostExcel(MultipartFile multfile) throws Exception {

    return postService.importAndCheckPostExcel(multfile,getUserSession());

  }


  @GetMapping("/importPostExcelToDatabase")
  @ApiOperation(value = "导入岗位入库")
  public ResponseResult importPostExcelToDatabase(@RequestParam("redisKey") String redisKey){

    return postService.importPostExcelToDatabase(redisKey,getUserSession());
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
    if (isEnable == null || isEnable == 0) {
      isEnable = 0;
    } else {
      isEnable = null;
    }
    List<Post> pageResult = postService.getPostGraphics(getUserSession(), layer, isContainsCompiler, isContainsActualMembers, postId, isEnable);
    return new ResponseResult(pageResult);
  }

}
