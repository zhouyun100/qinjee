package com.qinjee.masterdata.controller.organization;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.Post;
import com.qinjee.masterdata.model.entity.UserArchivePostRelation;
import com.qinjee.masterdata.model.vo.organization.PostVo;
import com.qinjee.masterdata.model.vo.organization.page.PostPageVo;
import com.qinjee.masterdata.service.organation.PostService;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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


  @ApiImplicitParams({
      @ApiImplicitParam(name = "pageSize", value = "每页大小", paramType = "query", dataType = "int", required = true, example = "10"),
      @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", dataType = "int", required = true, example = "1"),
      @ApiImplicitParam(name = "postId", value = "岗位Id", paramType = "query", dataType = "int", required = true, example = "1")
  })

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

  @ApiOperation(value = "未验证，根据查询条件导出Excel", notes = "未验证")
  @PostMapping("/downloadExcelByCondition")
  public ResponseResult downloadExcelByCondition(@RequestBody PostPageVo postPageVo, HttpServletResponse response) {
    return postService.downloadExcelByCondition(postPageVo, getUserSession(), response);
  }

  @ApiOperation(value = "未验证，根据选择的岗位id导出Excel", notes = "未验证")
  @GetMapping("/downloadExcelByPostId")
  public ResponseResult downloadExcelByPostId(@RequestParam("postIds") @ApiParam(value = "所选机构的编码", required = true) List<Integer> postIds, HttpServletResponse response) {
    return postService.downloadExcelByPostId(postIds, getUserSession(), response);
  }


  @ApiOperation(value = "未实现，导入Excel", notes = "未实现")
  @PostMapping("/uploadExcel")
  public ResponseResult uploadExcel(@ApiParam(value = "需要导入的Excel文件", required = true) MultipartFile file) {

    return null;
  }


}
