package com.qinjee.masterdata.controller.organization;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.Post;
import com.qinjee.masterdata.model.entity.UserArchivePostRelation;
import com.qinjee.masterdata.model.vo.organization.PostPageVo;
import com.qinjee.masterdata.model.vo.organization.PostVo;
import com.qinjee.masterdata.service.organation.PostService;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
    @ApiOperation(value = "分页查询岗位列表", notes = "高雄")
    public ResponseResult<PageResult<Post>> getPostList(@RequestBody PostPageVo postPageVo){
        return postService.getPostList(getUserSession(), postPageVo);
    }

    @GetMapping("/getAllPost")
    @ApiOperation(value = "根据机构id获取机构下所有的岗位", notes = "新增用户信息岗位下拉框的生成")
    public ResponseResult<List<Post>> getAllPost(@RequestParam("orgId")@ApiParam(name = "orgId",value = "机构id", example = "1", required = true) Integer orgId){
        return postService.getAllPost(getUserSession(), orgId);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "每页大小", paramType = "query", dataType = "int", required = true, example = "10"),
            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", dataType = "int", required = true, example = "1"),
            @ApiImplicitParam(name = "postId", value = "岗位Id", paramType = "query", dataType = "int", required = true, example = "1")
    })
    @ApiOperation(value = "根据岗位id查询员工档案岗位关系表", notes = "高雄")
    @GetMapping("/getUserArchivePostRelationList")
    public ResponseResult<PageResult<UserArchivePostRelation>> getUserArchivePostRelationList(Integer pageSize,
                                                                                              Integer currentPage,
                                                                                              Integer postId){
        return postService.getUserArchivePostRelationList(pageSize, currentPage, postId);
    }

    @ApiOperation(value = "新增岗位", notes = "高雄")
    @PostMapping("/addPost")
    public ResponseResult addPost(PostVo postVo){
        return postService.addPost(postVo, getUserSession());
    }

    @ApiOperation(value = "编辑岗位", notes = "高雄")
    @PostMapping("/editPost")
    public ResponseResult editPost(PostVo postVo){
        return postService.editPost(postVo, getUserSession());
    }

    @ApiOperation(value = "删除岗位", notes = "高雄")
    @PostMapping("/deletePost")
    public ResponseResult deletePost(@RequestBody  List<Integer> postIds){
        return postService.deletePost(getUserSession(), postIds);
    }

    @PostMapping("/lockPostByIds")
    @ApiOperation(value = "封存岗位", notes = "高雄")
    public ResponseResult lockPostByIds( @RequestBody List<Integer> postIds){
        return postService.sealPostByIds(postIds, Short.parseShort("0"), getUserSession());
    }

    @PostMapping("/unlockPostByIds")
    @ApiOperation(value = "解封岗位", notes = "高雄")
    public ResponseResult unlockPostByIds(@RequestBody List<Integer> postIds){
        return postService.sealPostByIds(postIds, Short.parseShort("1"), getUserSession());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "prePostId", value = "上个岗位id", paramType = "query", dataType = "int",  required = true, example = "0103"),
            @ApiImplicitParam(name = "midPostId", value = "需要排序岗位id", paramType = "query", dataType = "int", required = true, example = "0101"),
            @ApiImplicitParam(name = "nextPostId", value = "下一个岗位id", paramType = "query", dataType = "int", required = true, example = "0102"),
    })
    @GetMapping("/sortOrganization")
    @ApiOperation(value = "岗位排序", notes = "高雄")
    public ResponseResult sortOrganization(@RequestParam("prePostId")Integer prePostId,
                                         @RequestParam("midPostId")Integer midPostId,
                                         @RequestParam("nextPostId")Integer nextPostId){
        return postService.sortOrganization(prePostId, midPostId, nextPostId, getUserSession());
    }


    @ApiOperation(value = "下载模板", notes = "高雄")
    @GetMapping("/downloadTemplate")
    public ResponseResult downloadTemplate(HttpServletResponse response){
        return postService.downloadTemplate(response);
    }

    @ApiOperation(value = "复制岗位", notes = "高雄")
    @GetMapping("/copyPost")
    public ResponseResult copyPost(@ApiParam(value = "岗位id",required = true, allowMultiple = true) List<Integer> postIds,
                                   @ApiParam(value = "机构id", required = true, example = "1") Integer orgId){
        return postService.copyPost(postIds, getUserSession(), orgId);
    }

    @ApiOperation(value = "根据查询条件导出Excel", notes = "高雄")
    @PostMapping("/downloadExcelByCondition")
    public ResponseResult downloadExcelByCondition(@RequestBody PostPageVo postPageVo, HttpServletResponse response){
        return postService.downloadExcelByCondition(postPageVo, getUserSession(), response);
    }

    @ApiOperation(value = "根据选择的岗位id导出Excel", notes = "高雄")
    @GetMapping("/downloadExcelByPostId")
    public ResponseResult downloadExcelByPostId(@RequestParam("postIds") @ApiParam(value = "所选机构的编码",required = true) List<Integer> postIds, HttpServletResponse response){
        return postService.downloadExcelByPostId(postIds,getUserSession(), response);
    }


    @ApiOperation(value = "导入Excel", notes = "高雄")
    @PostMapping("/uploadExcel")
    public ResponseResult uploadExcel(@ApiParam(value = "需要导入的Excel文件", required = true) MultipartFile file){

        return null;
    }










}
