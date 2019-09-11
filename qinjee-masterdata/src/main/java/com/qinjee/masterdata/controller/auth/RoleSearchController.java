/**
 * 文件名：RoleSearch
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/10
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.controller.auth;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 周赟
 * @date 2019/9/10
 */
@Api(tags = "角色反查API")
@RestController
@RequestMapping("/roleSearch")
public class RoleSearchController extends BaseController{

    @ApiOperation(value="根据工号或姓名查询员工列表", notes="根据工号或姓名模糊查询员工列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "工号/姓名", required = true, dataType = "String")
    })
    @RequestMapping(value = "/searchArchiveListByUserName",method = RequestMethod.GET)
    public ResponseResult searchArchiveListByUserName(HttpServletRequest request, String userName) {

        return null;
    }

    @ApiOperation(value="根据档案ID查询角色列表", notes="根据档案ID查询角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "archiveId", value = "档案ID", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/searchRoleListByArchiveId",method = RequestMethod.GET)
    public ResponseResult searchRoleListByArchiveId(HttpServletRequest request, Integer archiveId) {

        return null;
    }


    @ApiOperation(value="修改用户角色", notes="修改用户角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "archiveId", value = "档案ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/updateArchiveRole",method = RequestMethod.GET)
    public ResponseResult updateArchiveRole(HttpServletRequest request, Integer archiveId, Integer roleId) {

        return null;
    }
}
