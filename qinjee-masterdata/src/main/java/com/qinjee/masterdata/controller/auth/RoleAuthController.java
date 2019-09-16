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
import com.qinjee.masterdata.model.entity.CustomArchiveField;
import com.qinjee.masterdata.model.entity.CustomArchiveTable;
import com.qinjee.masterdata.model.vo.auth.MenuVO;
import com.qinjee.masterdata.model.vo.auth.OrganizationVO;
import com.qinjee.masterdata.model.vo.auth.RoleDataLevelAuthVO;
import com.qinjee.masterdata.model.vo.auth.RoleGroupVO;
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
 * @author 周赟
 * @date 2019/9/10
 */
@Api(tags = "【权限管理】角色授权接口")
@RestController
@RequestMapping("/roleAuth")
public class RoleAuthController extends BaseController{

    @ApiOperation(value="角色树查询", notes="角色树查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/searchRoleTree",method = RequestMethod.GET)
    public ResponseResult<RoleGroupVO> searchRoleTree(String roleName) {

        return null;
    }

    @ApiOperation(value="新增角色组", notes="新增角色组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentRoleGroupId", value = "父级角色组ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "roleGroupName", value = "角色组名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/addRoleGroup",method = RequestMethod.GET)
    public ResponseResult addRoleGroup(Integer parentRoleGroupId, String roleGroupName) {

        return null;
    }

    @ApiOperation(value="新增角色", notes="新增角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleGroupId", value = "角色组ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/addRole",method = RequestMethod.GET)
    public ResponseResult addRole(Integer roleGroupId, String roleName) {

        return null;
    }

    @ApiOperation(value="修改角色", notes="修改角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "roleGroupId", value = "角色组ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/updateRole",method = RequestMethod.GET)
    public ResponseResult updateRole(Integer roleId, Integer roleGroupId, String roleName) {

        return null;
    }

    @ApiOperation(value="修改角色组", notes="修改角色组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleGroupId", value = "角色组ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "parentRoleGroupId", value = "父级角色组ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "roleGroupName", value = "角色组名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/updateRoleGroup",method = RequestMethod.GET)
    public ResponseResult updateRoleGroup(Integer roleGroupId, Integer parentRoleGroupId, String roleGroupName) {

        return null;
    }

    @ApiOperation(value="根据角色ID删除角色", notes="根据角色ID删除角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/delRole",method = RequestMethod.GET)
    public ResponseResult delRole(Integer roleId) {

        return null;
    }

    @ApiOperation(value="根据角色组ID删除角色组", notes="根据角色组ID删除角色组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleGroupId", value = "角色组ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/delRoleGroup",method = RequestMethod.GET)
    public ResponseResult delRoleGroup(Integer roleGroupId) {

        return null;
    }


    @ApiOperation(value="根据角色ID查询角色功能权限树", notes="根据角色ID查询菜单功能权限树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/searchRoleAuthTree",method = RequestMethod.GET)
    public ResponseResult<MenuVO> searchRoleAuthTree(Integer roleId) {

        return null;
    }


    @ApiOperation(value="修改角色功能权限", notes="修改角色功能权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "menuIdList", value = "功能ID集合", required = true, dataType = "int", allowMultiple = true)
    })
    @RequestMapping(value = "/updateRoleMenuAuth",method = RequestMethod.GET)
    public ResponseResult updateRoleMenuAuth(Integer roleId, List<Integer> menuIdList) {

        return null;
    }


    @ApiOperation(value="查询角色机构权限树", notes="根据角色ID查询机构权限树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/searchOrgAuthTree",method = RequestMethod.GET)
    public ResponseResult<OrganizationVO> searchOrgAuthTree(Integer roleId) {

        return null;
    }



    @ApiOperation(value="修改角色机构权限", notes="修改角色机构权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "orgIdList", value = "机构ID集合", required = true, dataType = "int", allowMultiple = true)
    })
    @RequestMapping(value = "/updateRoleOrgAuth",method = RequestMethod.GET)
    public ResponseResult updateRoleOrgAuth(Integer roleId, List<Integer> orgIdList) {

        return null;
    }


    @ApiOperation(value="根据角色ID查询角色自定义表列表", notes="根据角色ID查询角色自定义表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/searchCustomArchiveTableList",method = RequestMethod.GET)
    public ResponseResult<CustomArchiveTable> searchCustomArchiveTableList(Integer roleId) {

        return null;
    }


    @ApiOperation(value="根据字定义表ID查询自定义字段列表", notes="根据字段义表ID查询自定义字段列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableId", value = "自定义表ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/searchCustomArchiveTableFieldListByTableId",method = RequestMethod.GET)
    public ResponseResult<CustomArchiveField> searchCustomArchiveTableFieldListByTableId(Integer tableId) {

        return null;
    }


    @ApiOperation(value="根据角色ID查询角色自定义表字段列表", notes="根据角色ID查询角色自定义表字段列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/searchCustomArchiveTableFieldListByRoleId",method = RequestMethod.GET)
    public ResponseResult<CustomArchiveField> searchCustomArchiveTableFieldListByRoleId(Integer roleId) {

        return null;
    }


    @ApiOperation(value="修改角色自定义人员表字段权限", notes="修改角色自定义人员表字段权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "fieldIdList", value = "字段ID集合", required = true, dataType = "int", allowMultiple = true)
    })
    @RequestMapping(value = "/updateRoleCustomArchiveTableFieldAuth",method = RequestMethod.GET)
    public ResponseResult updateRoleCustomArchiveTableFieldAuth(Integer roleId, List<Integer> fieldIdList) {

        return null;
    }


    @ApiOperation(value="保存数据级权限定义", notes="保存数据级权限定义")
    @RequestMapping(value = "/saveRoleDataLevelAuth",method = RequestMethod.POST)
    public ResponseResult saveRoleDataLevelAuth(RoleDataLevelAuthVO roleDataLevelAuthVO) {

        return null;
    }

}
