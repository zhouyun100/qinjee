/**
 * 文件名：RoleAuthController
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
@Api(tags = "角色授权API")
@RestController
@RequestMapping("/roleAuth")
public class RoleAuthController extends BaseController{

    @ApiOperation(value="角色树查询", notes="角色树查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/searchRoleTree",method = RequestMethod.GET)
    public ResponseResult searchRoleTree(HttpServletRequest request, String roleName) {

        return null;
    }

    @ApiOperation(value="新增角色组", notes="新增角色组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentRoleGroupId", value = "父级角色组ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "roleGroupName", value = "角色组名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/addRoleGroup",method = RequestMethod.GET)
    public ResponseResult addRoleGroup(HttpServletRequest request, Integer parentRoleGroupId, String roleGroupName) {

        return null;
    }

    @ApiOperation(value="新增角色", notes="新增角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleGroupId", value = "角色组ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/addRole",method = RequestMethod.GET)
    public ResponseResult addRole(HttpServletRequest request, Integer roleGroupId, String roleName) {

        return null;
    }

    @ApiOperation(value="修改角色", notes="修改角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "roleGroupId", value = "角色组ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/updateRole",method = RequestMethod.GET)
    public ResponseResult updateRole(HttpServletRequest request,Integer roleId, Integer roleGroupId, String roleName) {

        return null;
    }

    @ApiOperation(value="修改角色组", notes="修改角色组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleGroupId", value = "角色组ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "parentRoleGroupId", value = "父级角色组ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "roleGroupName", value = "角色组名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/updateRoleGroup",method = RequestMethod.GET)
    public ResponseResult updateRoleGroup(HttpServletRequest request, Integer roleGroupId, Integer parentRoleGroupId, String roleGroupName) {

        return null;
    }

    @ApiOperation(value="删除角色", notes="删除角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/delRole",method = RequestMethod.GET)
    public ResponseResult delRole(HttpServletRequest request,Integer roleId) {

        return null;
    }

    @ApiOperation(value="删除角色组", notes="删除角色组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleGroupId", value = "角色组ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/delRoleGroup",method = RequestMethod.GET)
    public ResponseResult delRoleGroup(HttpServletRequest request, Integer roleGroupId) {

        return null;
    }


    @ApiOperation(value="查询角色功能权限树", notes="根据角色ID查询菜单功能权限树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/searchRoleAuthTree",method = RequestMethod.GET)
    public ResponseResult searchRoleAuthTree(HttpServletRequest request, Integer roleId) {

        return null;
    }


    @ApiOperation(value="查询角色机构权限树", notes="根据角色ID查询机构权限树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/searchOrgAuthTree",method = RequestMethod.GET)
    public ResponseResult searchOrgAuthTree(HttpServletRequest request, Integer roleId) {

        return null;
    }


    @ApiOperation(value="根据角色ID查询角色自定义表列表", notes="根据角色ID查询角色自定义表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/searchCustomArchiveTableList",method = RequestMethod.GET)
    public ResponseResult searchCustomArchiveTableList(HttpServletRequest request, Integer roleId) {

        return null;
    }


    @ApiOperation(value="根据字段义表ID查询自定义字段列表", notes="根据字段义表ID查询自定义字段列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableId", value = "自定义表ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/searchCustomArchiveTableFieldListByTableId",method = RequestMethod.GET)
    public ResponseResult searchCustomArchiveTableFieldListByTableId(HttpServletRequest request, Integer tableId) {

        return null;
    }


    @ApiOperation(value="根据角色ID查询角色自定义表字段列表", notes="根据角色ID查询角色自定义表字段列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/searchCustomArchiveTableFieldListByRoleId",method = RequestMethod.GET)
    public ResponseResult searchCustomArchiveTableFieldListByRoleId(HttpServletRequest request, Integer roleId) {

        return null;
    }


    @ApiOperation(value="添加数据级权限定义", notes="添加数据级权限定义")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "isLeftBrackets", value = "角色ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "fieldId", value = "角色ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "operateSymbol", value = "角色ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "fieldValue", value = "角色ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "isRightBrackets", value = "角色ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "linkSymbol", value = "角色ID", required = true, dataType = "String")
    })
    @RequestMapping(value = "/addRoleDataLevelAuth",method = RequestMethod.GET)
    public ResponseResult addRoleDataLevelAuth(HttpServletRequest request, Integer roleId, Integer isLeftBrackets, Integer fieldId,
                                               String operateSymbol, String fieldValue , Integer isRightBrackets, String linkSymbol) {

        return null;
    }

}
