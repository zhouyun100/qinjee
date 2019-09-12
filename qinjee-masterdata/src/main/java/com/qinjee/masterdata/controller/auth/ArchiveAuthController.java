/**
 * 文件名：ArchiveAuthController
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
import java.util.List;

/**
 * @author 周赟
 * @date 2019/9/10
 */
@Api(tags = "用户授权API")
@RestController
@RequestMapping("/archiveAuth")
public class ArchiveAuthController extends BaseController {

    @ApiOperation(value="角色树查询", notes="根据角色名称模糊查询角色树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/searchRoleTree",method = RequestMethod.GET)
    public ResponseResult searchRoleTree(HttpServletRequest request, String roleName) {

        return null;
    }

    @ApiOperation(value="根据角色ID查询员工列表", notes="根据角色ID查询员工")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/searchArchiveListByRoleId",method = RequestMethod.GET)
    public ResponseResult searchArchiveListByRoleId(HttpServletRequest request, Integer roleId) {

        return null;
    }

    @ApiOperation(value="角色新增员工", notes="角色新增员工")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "archiveIdList", value = "档案ID集合", required = true, dataType = "int", allowMultiple = true)
    })
    @RequestMapping(value = "/addArchiveRole",method = RequestMethod.GET)
    public ResponseResult addArchiveRole(HttpServletRequest request, Integer roleId, List<Integer> archiveIdList) {

        return null;
    }

    @ApiOperation(value="角色移除员工", notes="角色移除员工")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "archiveIdList", value = "档案ID集合", required = true, dataType = "int", allowMultiple = true)
    })
    @RequestMapping(value = "/delArchiveRole",method = RequestMethod.GET)
    public ResponseResult delArchiveRole(HttpServletRequest request, Integer roleId, List<Integer> archiveIdList) {

        return null;
    }
}
