/**
 * 文件名：AuthMoveController
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
@Api(tags = "权限移交API")
@RestController
@RequestMapping("/authHandover")
public class AuthHandoverController extends BaseController{

    @ApiOperation(value="查询角色功能权限树", notes="根据角色ID查询菜单功能权限树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/searchRoleAuthTree",method = RequestMethod.GET)
    public ResponseResult searchRoleAuthTree(HttpServletRequest request, Integer roleId) {

        return null;
    }

    @ApiOperation(value="查询角色机构权限树", notes="根据角色ID查询机构权限树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/searchOrgAuthTree",method = RequestMethod.GET)
    public ResponseResult searchOrgAuthTree(HttpServletRequest request, Integer roleId) {

        return null;
    }

    @ApiOperation(value="角色回收", notes="根据档案回收角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "archiveId", value = "档案ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/roleRecoveryByArchiveId",method = RequestMethod.GET)
    public ResponseResult roleRecoveryByArchiveId(HttpServletRequest request, Integer archiveId, Integer roleId) {

        return null;
    }

    @ApiOperation(value="机构回收", notes="根据档案回收机构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "archiveId", value = "档案ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "orgId", value = "机构ID", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/orgRecoveryByArchiveId",method = RequestMethod.GET)
    public ResponseResult orgRecoveryByArchiveId(HttpServletRequest request, Integer archiveId, Integer orgId) {

        return null;
    }

    @ApiOperation(value="角色移交", notes="根据档案移交角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "handoverArchiveId", value = "移交人档案ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "acceptArchiveId", value = "接收人档案ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/roleHandoverByArchiveId",method = RequestMethod.GET)
    public ResponseResult roleHandoverByArchiveId(HttpServletRequest request, Integer handoverArchiveId, Integer acceptArchiveId, Integer roleId) {

        return null;
    }

    @ApiOperation(value="机构移交", notes="根据档案移交机构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "handoverArchiveId", value = "移交人档案ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "acceptArchiveId", value = "接收人档案ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "orgId", value = "机构ID", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/orgHandoverByArchiveId",method = RequestMethod.GET)
    public ResponseResult orgHandoverByArchiveId(HttpServletRequest request, Integer handoverArchiveId, Integer acceptArchiveId, Integer orgId) {

        return null;
    }

    @ApiOperation(value="角色托管", notes="根据档案托管角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "trusteeshipArchiveId", value = "托管人档案ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "acceptArchiveId", value = "接收人档案ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "trusteeshipBeginTime", value = "托管开始时间", required = true, dataType = "String"),
            @ApiImplicitParam(name = "trusteeshipEndTime", value = "托管结束时间", required = true, dataType = "String"),
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/roleTrusteeshipByArchiveId",method = RequestMethod.GET)
    public ResponseResult roleTrusteeshipByArchiveId(HttpServletRequest request, Integer trusteeshipArchiveId, Integer acceptArchiveId, String trusteeshipBeginTime, String trusteeshipEndTime, Integer roleId) {

        return null;
    }

    @ApiOperation(value="机构托管", notes="根据档案移交机构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "trusteeshipArchiveId", value = "托管人档案ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "acceptArchiveId", value = "接收人档案ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "trusteeshipBeginTime", value = "托管开始时间", required = true, dataType = "String"),
            @ApiImplicitParam(name = "trusteeshipEndTime", value = "托管结束时间", required = true, dataType = "String"),
            @ApiImplicitParam(name = "orgId", value = "机构ID", required = true, dataType = "Integer")
    })
    @RequestMapping(value = "/orgTrusteeshipByArchiveId",method = RequestMethod.GET)
    public ResponseResult orgTrusteeshipByArchiveId(HttpServletRequest request, Integer trusteeshipArchiveId, Integer acceptArchiveId, String trusteeshipBeginTime, String trusteeshipEndTime, Integer orgId) {

        return null;
    }
}
