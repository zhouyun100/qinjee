package com.qinjee.masterdata.controller.staff;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.QueryScheme;
import com.qinjee.masterdata.model.entity.QuerySchemeField;
import com.qinjee.masterdata.model.entity.QuerySchemeSort;
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

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/staffarc")
@Api(tags = "【人员管理】档案相关接口")
public class StaffArchiveController extends BaseController {
    /**
     * 新增人员岗位关系，初期只涉及到是否兼职
     */

    @RequestMapping(value = "/insertUserArchivePostRelation", method = RequestMethod.POST)
    @ApiOperation(value = "新增人员岗位关系，初期只涉及到是否兼职", notes = "hkt")
    @ApiImplicitParam(name = "UserArchivePostRelation", value = "人员档案关系表", paramType = "form", required = true)
    public ResponseResult<Boolean> insertUserArchivePostRelation(UserArchivePostRelation userArchivePostRelation) {
        ResponseResult<Boolean> responseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return responseResult;
    }

    /**
     * 删除人员岗位关系，初期只涉及到是否兼职
     */

    @RequestMapping(value = "/deleteUserArchivePostRelation", method = RequestMethod.GET)
    @ApiOperation(value = "删除人员岗位关系，初期只涉及到是否兼职", notes = "hkt")
    @ApiImplicitParam(name = "UserArchivePostRelationId", value = "人员档案关系表id", paramType = "query", required = true)
    public ResponseResult<Boolean> deleteUserArchivePostRelation(UserArchivePostRelation userArchivePostRelation) {
        ResponseResult<Boolean> responseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return responseResult;
    }

    /**
     * 修改人员岗位关系，初期值涉及到是否兼职
     */

    @RequestMapping(value = "/updateUserArchivePostRelation", method = RequestMethod.GET)
    @ApiOperation(value = "修改人员岗位关系，初期只涉及到是否兼职", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "UserArchivePostRelationId", value = "人员档案关系表id", paramType = "query", required = true),
            @ApiImplicitParam(name = "UserArchivePostRelation", value = "人员档案关系表", paramType = "form", required = true)
    })

    public ResponseResult<Boolean> updateUserArchivePostRelation(Integer id, UserArchivePostRelation userArchivePostRelation) {
        ResponseResult<Boolean> responseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return responseResult;
    }

    /**
     * 展示人员岗位关系，初期只涉及到是否兼职
     */

    @RequestMapping(value = "/selectUserArchivePostRelation", method = RequestMethod.GET)
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

    /**
     * 新增查询方案
     */

    @RequestMapping(value = "/insertQueryScheme", method = RequestMethod.POST)
    @ApiOperation(value = "新增查询方案", notes = "hkt")
    @ApiImplicitParam(name = "insertQueryScheme", value = "查询方案", paramType = "form", required = true)
    public ResponseResult<Boolean> insertUserArchivePostRelation(QueryScheme queryScheme) {
        ResponseResult<Boolean> responseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return responseResult;
    }

    /**
     * 删除查询方案
     */

    @RequestMapping(value = "/deleteQueryScheme", method = RequestMethod.GET)
    @ApiOperation(value = "删除查询方案", notes = "hkt")
    @ApiImplicitParam(name = "querySchemeId", value = "查询方案id", paramType = "query", required = true)
    public ResponseResult<Boolean> deleteQueryScheme(Integer id) {
        ResponseResult<Boolean> responseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return responseResult;
    }

    /**
     * 修改查询方案
     */

    @RequestMapping(value = "/updateQueryScheme", method = RequestMethod.GET)
    @ApiOperation(value = "修改查询方案", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "QuerySchemeId", value = "修改查询方案id", paramType = "query", required = true),
            @ApiImplicitParam(name = "QueryScheme", value = "查询方案", paramType = "form", required = true)
    })
    public ResponseResult<Boolean> updateQueryScheme(Integer id, QueryScheme queryScheme) {
        ResponseResult<Boolean> responseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return responseResult;
    }

    /**
     * 展示查询方案
     * 根据排序字段确认顺序，根据查询方案id找到显示字段与排序字段
     */


    @RequestMapping(value = "/selectQueryScheme", method = RequestMethod.GET)
    @ApiOperation(value = "展示查询方案", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "查询方案id", paramType = "query", required = true)

    public ResponseResult<QueryScheme> selectUserArchivePostRelation(Integer id) {
        QueryScheme queryScheme = new QueryScheme();
        ResponseResult<QueryScheme> querySchemeResponseResult = new ResponseResult<>(queryScheme, CommonCode.SUCCESS);
        return querySchemeResponseResult;
    }

    /**
     * 新增查询方案字段表，支持批量添加
     */

    @RequestMapping(value = "/insertQuerySchemeFieldList", method = RequestMethod.POST)
    @ApiOperation(value = "新增查询方案字段，支持批量添加", notes = "hkt")
    @ApiImplicitParam(name = "QuerySchemeFieldList", value = "查询方案字段集合", paramType = "form", required = true)
    public ResponseResult<Boolean> insertUserArchivePostRelation(List<QuerySchemeField> list) {
        ResponseResult<Boolean> responseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return responseResult;
    }

    /**
     * 新增查询方案排序表，支持批量添加
     */

    @RequestMapping(value = "/insertQuerySchemeSortList", method = RequestMethod.POST)
    @ApiOperation(value = "新增查询方案排序表，支持批量添加", notes = "hkt")
    @ApiImplicitParam(name = "QuerySchemeSortList", value = "查询方案排序集合", paramType = "form", required = true)
    public ResponseResult<Boolean> insertQuerySchemeSortList(List<QuerySchemeSort> list) {
        ResponseResult<Boolean> responseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return responseResult;
    }


}
