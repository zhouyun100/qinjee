package com.qinjee.masterdata.controller.organation;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.Organation;
import com.qinjee.masterdata.model.entity.Post;
import com.qinjee.masterdata.model.entity.UserArchivePostRelation;
import com.qinjee.masterdata.model.vo.organation.PostPageVo;
import com.qinjee.masterdata.model.vo.organation.PostVo;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月11日 15:17:00
 */
@Api(tags = "岗位相关接口")
@RestController
@RequestMapping("/post")
public class PostController extends BaseController {

    @GetMapping("/getPostTree")
    @ApiOperation(value = "根据是否封存查询用户下所有的机构岗位,树形结构展示",notes = "高雄")
    public ResponseResult<PageResult<Organation>> getPostTree(@RequestParam("isEnable") @ApiParam(value = "是否含有封存 0不含有、1含有",example = "0") Short isEnable){
        //调用机构service查询组织机构树的方法 ,通过标识判断是否要查询岗位
        return null;
    }

    @GetMapping("/getPostList")
    @ApiOperation(value = "分页查询岗位列表", notes = "高雄")
    public ResponseResult<PageResult<Post>> getPostList(PostPageVo postPageVo){

        return null;
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
        return null;
    }


    @ApiOperation(value = "新增岗位", notes = "高雄")
    @PostMapping("/addPost")
    public ResponseResult addPost(PostVo postVo){

        return null;
    }

    @ApiOperation(value = "编辑岗位", notes = "高雄")
    @PostMapping("/editPost")
    public ResponseResult editPost(PostVo postVo){

        return null;
    }

    @ApiOperation(value = "删除岗位", notes = "高雄")
    @GetMapping("/deletePost")
    @ApiImplicitParam(name="postIds", value = "选择的岗位id", paramType = "query", dataType = "int", allowMultiple = true, required = true)
    public ResponseResult deletePost(List<Integer> postIds){

        return null;
    }

    @GetMapping("/sealPostByIds")
    @ApiOperation(value = "封存/封存机构", notes = "高雄")
    public ResponseResult sealPostByIds(@RequestParam("orgCode") @ApiParam(value = "机构编码",example = "1",required = true) List<String> postIds,
                                             @RequestParam("isEnable") @ApiParam(value = "0 封存、1 解封",example = "0") Short isEnable){

        return null;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "prePostId", value = "上个岗位id", paramType = "query", dataType = "int",  required = true, example = "0103"),
            @ApiImplicitParam(name = "midPostId", value = "需要排序岗位id", paramType = "query", dataType = "int", required = true, example = "0101"),
            @ApiImplicitParam(name = "nextPostId", value = "下一个岗位id", paramType = "query", dataType = "int", required = true, example = "0102"),
    })
    @GetMapping("/sortOrganation")
    @ApiOperation(value = "岗位排序", notes = "高雄")
    public ResponseResult sortOrganation(@RequestParam("prePostId")Integer prePostId,
                                         @RequestParam("midPostId")Integer midPostId,
                                         @RequestParam("nextPostId")Integer nextPostId){

        return null;
    }


    @ApiOperation(value = "下载模板", notes = "高雄")
    @GetMapping("/downloadTemplate")
    public ResponseResult downloadTemplate(){

        return null;
    }

    @ApiOperation(value = "复制岗位", notes = "高雄")
    @GetMapping("/copyPost")
    public ResponseResult copyPost(@RequestParam("postIds") @ApiParam(value = "岗位id",required = true) List<Integer> postIds){

        return null;
    }

    @ApiOperation(value = "根据查询条件导出Excel", notes = "高雄")
    @PostMapping("/downloadExcelByCondition")
    public ResponseResult downloadExcelByCondition(@RequestBody PostPageVo postPageVo){

        return null;
    }

    @ApiOperation(value = "根据选择的岗位id导出Excel", notes = "高雄")
    @GetMapping("/downloadExcelByPostId")
    public ResponseResult downloadExcelByPostId(@RequestParam("postIds") @ApiParam(value = "所选机构的编码",required = true) List<Integer> postIds){

        return null;
    }


    @ApiOperation(value = "导入Excel", notes = "高雄")
    @PostMapping("/uploadExcel")
    public ResponseResult uploadExcel(@ApiParam(value = "需要导入的Excel文件", required = true) MultipartFile file){

        return null;
    }










}
