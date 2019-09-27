package com.qinjee.masterdata.controller.organization;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.PostInstructions;
import com.qinjee.masterdata.service.organation.PostInstructionsService;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月11日 18:13:00
 */
@RestController
@RequestMapping("/postInstructions")
@Api(tags = "【机构管理】岗位说明书表接口")
public class PostInstructionsController extends BaseController {

    @Autowired
    private PostInstructionsService postInstructionsService;

    @GetMapping("showPostInstructions")
    @ApiOperation(value = "展示岗位说明书", notes = "高雄")
    public ResponseResult<PostInstructions> showPostInstructions(@ApiParam(value = "岗位Id", example = "1", required = true) Integer postId){
        return postInstructionsService.showPostInstructions(postId);
    }

    @PostMapping("uploadInstructions")
    @ApiOperation(value = "导入岗位说明书", notes = "高雄")
    public ResponseResult uploadInstructions(@ApiParam(value = "导入的文件", required = true, allowMultiple = true) MultipartFile file){
        return postInstructionsService.uploadInstructions(getUserSession(), file);
    }


    @GetMapping("downloadInstructions")
    @ApiOperation(value = "下载岗位说明书", notes = "高雄")
    public ResponseResult downloadInstructions(@RequestParam("instructionId") @ApiParam(value = "岗位说明书id", required = true, example = "1") Integer instructionId,
                                               HttpServletResponse response){
        return postInstructionsService.downloadInstructions(instructionId, response);
    }













}


















