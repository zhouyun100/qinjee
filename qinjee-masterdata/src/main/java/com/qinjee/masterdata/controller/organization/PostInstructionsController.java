package com.qinjee.masterdata.controller.organization;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.PostInstructions;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("showPostInstructions")
    @ApiOperation(value = "展示岗位说明书", notes = "高雄")
    public ResponseResult<PostInstructions> showPostInstructions(){

        return null;
    }

    @GetMapping("uploadInstructions")
    @ApiOperation(value = "导入岗位说明书", notes = "高雄")
    public ResponseResult uploadInstructions(@ApiParam(value = "导入的excel", required = true) MultipartFile file){

        return null;
    }


    @GetMapping("downloadInstructions")
    @ApiOperation(value = "下载岗位说明书", notes = "高雄")
    public ResponseResult downloadInstructions(@ApiParam(value = "岗位说明书id", required = true) Integer instructionId){

        return null;
    }













}


















