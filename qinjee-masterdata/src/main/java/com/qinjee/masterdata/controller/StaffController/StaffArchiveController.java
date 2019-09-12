package com.qinjee.masterdata.controller.StaffController;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.UserArchivePostRelation;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/staffarc")
@Api(tags = "人员档案相关接口")
public class StaffArchiveController extends BaseController {
    //新增人员岗位关系，初期只涉及到是否兼职
    @RequestMapping(value = "/insertUserArchivePostRelation", method = RequestMethod.POST)
    @ApiOperation(value = "新增人员岗位关系，初期只涉及到是否兼职", notes = "hkt")
    @ApiImplicitParam(name = "UserArchivePostRelation", value = "人员档案关系表", paramType = "form", required = true)
    public ResponseResult<Boolean> insertUserArchivePostRelation(UserArchivePostRelation userArchivePostRelation) {
        ResponseResult<Boolean> responseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return responseResult;
    }
    //删除人员岗位关系，初期只涉及到是否兼职
    @RequestMapping(value = "/deletetUserArchivePostRelation", method = RequestMethod.POST)
    @ApiOperation(value = "删除人员岗位关系，初期只涉及到是否兼职", notes = "hkt")
    @ApiImplicitParam(name = "UserArchivePostRelationId", value = "人员档案关系表id", paramType = "query", required = true)
    public ResponseResult<Boolean> deleteUserArchivePostRelation(UserArchivePostRelation userArchivePostRelation) {
        ResponseResult<Boolean> responseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return responseResult;
    }
    //修改人员岗位关系，初期值涉及到是否兼职
    @RequestMapping(value = "/updateUserArchivePostRelation", method = RequestMethod.POST)
    @ApiOperation(value = "修改人员岗位关系，初期只涉及到是否兼职", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "UserArchivePostRelationId", value = "人员档案关系表id", paramType = "query", required = true),
            @ApiImplicitParam(name = "UserArchivePostRelation", value = "人员档案关系表", paramType = "form", required = true)
    })

    public ResponseResult<Boolean> updateUserArchivePostRelation(Integer id,UserArchivePostRelation userArchivePostRelation) {
        ResponseResult<Boolean> responseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return responseResult;
    }
    //展示人员岗位关系，初期值涉及到是否兼职
    @RequestMapping(value = "/selectUserArchivePostRelation", method = RequestMethod.POST)
    @ApiOperation(value = "展示人员岗位关系，初期只涉及到是否兼职", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "number", value = "当前页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pagesize", value = "页大小", paramType = "form", required = true),
            @ApiImplicitParam(name = "UserArchive", value = "员工档案id", paramType = "query", required = true)
    })
    public ResponseResult<PageResult<List<UserArchivePostRelation>>> selectUserArchivePostRelation(UserArchivePostRelation userArchivePostRelation) {
        PageResult<List<UserArchivePostRelation>> list = new PageResult<>();
        ResponseResult<PageResult<List<UserArchivePostRelation>>> pageResultResponseResult = new ResponseResult<PageResult<List<UserArchivePostRelation>>>(list, CommonCode.SUCCESS);
        return pageResultResponseResult;
    }


}
