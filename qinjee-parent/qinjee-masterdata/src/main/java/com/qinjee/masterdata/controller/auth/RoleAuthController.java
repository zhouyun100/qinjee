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
import com.qinjee.masterdata.model.entity.Role;
import com.qinjee.masterdata.model.vo.auth.*;
import com.qinjee.masterdata.service.auth.ArchiveAuthService;
import com.qinjee.masterdata.service.auth.RoleAuthService;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 周赟
 * @date 2019/9/10
 */
@Api(tags = "【权限管理】角色授权接口")
@RestController
@RequestMapping("/roleAuth")
public class RoleAuthController extends BaseController{

    private static Logger logger = LogManager.getLogger(RoleAuthController.class);

    private ResponseResult responseResult;

    @Autowired
    private ArchiveAuthService archiveAuthService;

    @Autowired
    private RoleAuthService roleAuthService;

    @ApiOperation(value="角色树查询", notes="角色树查询")
    @RequestMapping(value = "/searchRoleTree",method = RequestMethod.POST)
    public ResponseResult<RoleGroupVO> searchRoleTree() {
        userSession = getUserSession();
        if(userSession == null){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("Session失效！");
            return responseResult;
        }
        try{
            List<RoleGroupVO> roleGroupList = archiveAuthService.searchRoleTree(userSession.getCompanyId());
            if(CollectionUtils.isEmpty(roleGroupList)){
                logger.info("searchRoleTree fail！companyId={},roleGroupList={}", userSession.getCompanyId(), roleGroupList);
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("当前登录用户角色树查询结果为空！");
            }else{
                logger.info("searchRoleTree success！companyId={},roleGroupList={}", userSession.getCompanyId(), roleGroupList);
                responseResult = ResponseResult.SUCCESS();
                responseResult.setResult(roleGroupList);
            }
        }catch (Exception e){
            logger.info("searchRoleTree exception! companyId={};exception={}", userSession.getCompanyId(), e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("当前登录用户角色树查询异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="新增角色组", notes="新增角色组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleGroupName", value = "角色组名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/addRoleGroup",method = RequestMethod.POST)
    public ResponseResult addRoleGroup(String roleGroupName) {
        if(StringUtils.isEmpty(roleGroupName)){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("角色组名称或父角色不能为空!");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            int resultNumber = roleAuthService.addRoleGroup(roleGroupName, userSession.getCompanyId(), userSession.getArchiveId());
            if(resultNumber > 0){
                logger.info("addRoleGroup success！roleGroupName={},operatorId={}", roleGroupName, userSession.getArchiveId());
                responseResult = ResponseResult.SUCCESS();
            }else{
                logger.info("addRoleGroup fail！roleGroupName={},operatorId={}", roleGroupName, userSession.getArchiveId());
                responseResult = ResponseResult.FAIL();
            }

        }catch (Exception e){
            logger.info("addRoleGroup exception！roleGroupName={},exception={}", roleGroupName, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("新增角色组异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="新增角色", notes="新增角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleGroupId", value = "角色组ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/addRole",method = RequestMethod.POST)
    public ResponseResult addRole(Integer roleGroupId, String roleName) {
        if(null == roleGroupId || StringUtils.isEmpty(roleName)){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("角色名称或父角色不能为空!");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            int resultNumber = roleAuthService.addRole(roleGroupId,roleName,userSession.getCompanyId(),userSession.getArchiveId());
            if(resultNumber > 0){
                logger.info("addRole success！roleGroupId={},roleName={},operatorId={}", roleGroupId, roleName, userSession.getArchiveId());
                responseResult = ResponseResult.SUCCESS();
            }else{
                logger.info("addRole fail！roleGroupId={},roleName={},operatorId={}", roleGroupId, roleName, userSession.getArchiveId());
                responseResult = ResponseResult.FAIL();
            }

        }catch (Exception e){
            logger.info("addRole exception！roleGroupId={},roleName={},exception={}", roleGroupId, roleName, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("新增角色异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="修改角色", notes="修改角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "roleGroupId", value = "角色组ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/updateRole",method = RequestMethod.POST)
    public ResponseResult updateRole(Integer roleId, Integer roleGroupId, String roleName) {
        if(null == roleId || null == roleGroupId || StringUtils.isEmpty(roleName)){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("角色或父角色不能为空!");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            int resultNumber = roleAuthService.updateRole(roleId,roleGroupId,roleName,userSession.getArchiveId());
            if(resultNumber > 0){
                logger.info("updateRole success！roleId={},roleGroupId={},roleName={},operatorId={}", roleId, roleGroupId, roleName, userSession.getArchiveId());
                responseResult = ResponseResult.SUCCESS();
            }else{
                logger.info("updateRole fail！roleId={},roleGroupId={},roleName={},operatorId={}", roleId, roleGroupId, roleName, userSession.getArchiveId());
                responseResult = ResponseResult.FAIL();
            }

        }catch (Exception e){
            logger.info("updateRole exception！roleId={},roleGroupId={},roleName={},exception={}", roleId, roleGroupId, roleName, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("修改角色异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="根据角色ID查询角色详情", notes="根据角色ID查询角色详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/searchRoleDetailByRoleId",method = RequestMethod.POST)
    public ResponseResult<Role> searchRoleDetailByRoleId(Integer roleId) {
        if(null == roleId){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("角色ID不能为空!");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            Role role = roleAuthService.searchRoleDetailByRoleId(roleId);
            if(role != null){
                logger.info("searchRoleDetailByRoleId success！roleId={}", roleId);
                responseResult = ResponseResult.SUCCESS();
                responseResult.setResult(role);
            }else{
                logger.info("searchRoleDetailByRoleId fail！roleId={}", roleId);
                responseResult = ResponseResult.FAIL();
            }

        }catch (Exception e){
            logger.info("searchRoleDetailByRoleId exception！roleId={},roleGroupId={},roleName={},exception={}", roleId);
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据角色ID查询角色详情异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="根据角色ID修改角色是否自动授权新增子集机构权限", notes="根据角色ID修改角色自动授权新增子集机构权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "isAutoAuthChildOrg", value = "是否自动授权新增子集机构权限(1:是;0:否)", required = true, dataType = "int")
    })
    @RequestMapping(value = "/updateRoleAutoAuthChildOrgByRoleId",method = RequestMethod.POST)
    public ResponseResult updateRoleAutoAuthChildOrgByRoleId(Integer roleId, Integer isAutoAuthChildOrg) {
        if(null == roleId || null == isAutoAuthChildOrg){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("角色ID或是否授权不能为空!");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            int resultNumber = roleAuthService.updateRoleAutoAuthChildOrgByRoleId(roleId,isAutoAuthChildOrg,userSession.getArchiveId());
            if(resultNumber > 0){
                logger.info("updateRoleAutoAuthChildOrgByRoleId success！roleId={},isAutoAuthChildOrg={},operatorId={}", roleId, isAutoAuthChildOrg, userSession.getArchiveId());
                responseResult = ResponseResult.SUCCESS();
            }else{
                logger.info("updateRoleAutoAuthChildOrgByRoleId fail！roleId={},isAutoAuthChildOrg={},operatorId={}", roleId, isAutoAuthChildOrg, userSession.getArchiveId());
                responseResult = ResponseResult.FAIL();
            }

        }catch (Exception e){
            logger.info("updateRoleAutoAuthChildOrgByRoleId exception！roleId={},isAutoAuthChildOrg={},exception={}", roleId, isAutoAuthChildOrg, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据角色ID修改角色是否自动授权新增子集机构权限异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="修改角色组", notes="修改角色组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleGroupId", value = "角色组ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "roleGroupName", value = "角色组名称", required = true, dataType = "String")
    })
    @RequestMapping(value = "/updateRoleGroup",method = RequestMethod.POST)
    public ResponseResult updateRoleGroup(Integer roleGroupId, String roleGroupName) {
        if(null == roleGroupId || StringUtils.isEmpty(roleGroupName)){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("角色组不能为空!");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            int resultNumber = roleAuthService.updateRoleGroup(roleGroupId, roleGroupName, userSession.getArchiveId());
            if(resultNumber > 0){
                logger.info("updateRole success！roleGroupId={},roleGroupName={},operatorId={}", roleGroupId, roleGroupName, userSession.getArchiveId());
                responseResult = ResponseResult.SUCCESS();
            }else{
                logger.info("updateRole fail！roleGroupId={},roleGroupName={},operatorId={}", roleGroupId, roleGroupName, userSession.getArchiveId());
                responseResult = ResponseResult.FAIL();
            }

        }catch (Exception e){
            logger.info("updateRole exception！roleGroupId={},roleGroupName={},exception={}", roleGroupId, roleGroupName, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("修改角色组异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="根据角色ID删除角色", notes="根据角色ID删除角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/delRole",method = RequestMethod.POST)
    public ResponseResult delRole(Integer roleId) {
        if(null == roleId){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("角色ID不能为空!");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            int resultNumber = roleAuthService.delRole(roleId, userSession.getArchiveId());
            if(resultNumber > 0){
                logger.info("delRole success！roleId={},operatorId={}", roleId,userSession.getArchiveId());
                responseResult = ResponseResult.SUCCESS();
            }else{
                logger.info("delRole fail！roleId={},operatorId={}", roleId, userSession.getArchiveId());
                responseResult = ResponseResult.FAIL();
            }

        }catch (Exception e){
            logger.info("delRole exception！roleId={},exception={}", roleId, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据角色ID删除角色异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="根据角色组ID删除角色组", notes="根据角色组ID删除角色组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleGroupId", value = "角色组ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/delRoleGroup",method = RequestMethod.POST)
    public ResponseResult delRoleGroup(Integer roleGroupId) {
        if(null == roleGroupId){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("角色组ID不能为空!");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            int resultNumber = roleAuthService.delRoleGroup(roleGroupId, userSession.getArchiveId());
            if(resultNumber > 0){
                logger.info("updateRole success！roleGroupId={},operatorId={}", roleGroupId,userSession.getArchiveId());
                responseResult = ResponseResult.SUCCESS();
            }else{
                logger.info("updateRole fail！roleGroupId={},operatorId={}", roleGroupId, userSession.getArchiveId());
                responseResult = ResponseResult.FAIL();
            }

        }catch (Exception e){
            logger.info("updateRole exception！roleGroupId={},exception={}", roleGroupId, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据角色组ID删除角色组异常！");
        }
        return responseResult;
    }


    @ApiOperation(value="根据角色ID查询角色功能权限树", notes="根据角色ID查询菜单功能权限树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/searchRoleAuthTree",method = RequestMethod.POST)
    public ResponseResult<MenuVO> searchRoleAuthTree(Integer roleId) {
        if(null == roleId){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("角色ID不能为空!");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            List<MenuVO> menuList = roleAuthService.searchRoleAuthTree(userSession.getArchiveId(), roleId, userSession.getCompanyId());
            if(CollectionUtils.isEmpty(menuList)){
                logger.info("searchRoleAuthTree fail！roleId={},menuList={}", roleId,menuList);
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("查询角色功能权限树结果为空！");
            }else {
                logger.info("searchRoleAuthTree success！roleId={},menuList={}", roleId,menuList);
                responseResult = ResponseResult.SUCCESS();
                responseResult.setResult(menuList);
            }
        }catch (Exception e){
            logger.info("searchRoleAuthTree exception！roleId={},exception={}", roleId, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据角色ID查询菜单功能权限树异常！");
        }
        return responseResult;
    }


    @ApiOperation(value="修改角色功能权限", notes="修改角色功能权限")
    @RequestMapping(value = "/updateRoleMenuAuth",method = RequestMethod.POST)
    public ResponseResult updateRoleMenuAuth(@RequestBody @ApiParam(value = "请求参数：\nroleId：角色ID\nmenuIdList：功能ID集合")RequestRoleAuthVO requestRoleAuthVO) {
        if(null == requestRoleAuthVO.getRoleId() || CollectionUtils.isEmpty(requestRoleAuthVO.getMenuIdList())){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("角色或菜单不能为空!");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            roleAuthService.updateRoleMenuAuth(requestRoleAuthVO.getRoleId(), requestRoleAuthVO.getMenuIdList(), userSession.getArchiveId());
            logger.info("updateRoleMenuAuth success！roleId={}, menuIdList={}, operatorId={}", requestRoleAuthVO.getRoleId(), requestRoleAuthVO.getMenuIdList(), userSession.getArchiveId());
            responseResult = ResponseResult.SUCCESS();

        }catch (Exception e){
            logger.info("updateRoleMenuAuth exception！roleId={}, menuIdList={}, exception={}", requestRoleAuthVO.getRoleId(), requestRoleAuthVO.getMenuIdList(), e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("修改角色功能权限异常！");
        }
        return responseResult;
    }


    @ApiOperation(value="根据角色ID查询角色机构权限树", notes="根据角色ID查询角色机构权限树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/searchOrgAuthTree",method = RequestMethod.POST)
    public ResponseResult<OrganizationVO> searchOrgAuthTree(Integer roleId) {
        if(null == roleId){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("角色ID不能为空!");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            List<OrganizationVO> organizationList = roleAuthService.searchOrgAuthTree(userSession.getArchiveId(), roleId);
            if(CollectionUtils.isEmpty(organizationList)){
                logger.info("searchOrgAuthTree fail！operatorId={},roleId={},organizationList={}", userSession.getArchiveId(), roleId, organizationList);
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("查询角色机构树结果为空！");
            }else {
                logger.info("searchOrgAuthTree success！operatorId={},roleId={},organizationList={}", userSession.getArchiveId(), roleId, organizationList);
                responseResult = ResponseResult.SUCCESS();
                responseResult.setResult(organizationList);
            }
        }catch (Exception e){
            logger.info("searchOrgAuthTree exception！roleId={},exception={}", roleId, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据角色ID查询机构树异常！");
        }
        return responseResult;
    }


    @ApiOperation(value="修改角色机构权限", notes="修改角色机构权限")
    @RequestMapping(value = "/updateRoleOrgAuth",method = RequestMethod.POST)
    public ResponseResult updateRoleOrgAuth(@RequestBody @ApiParam(value = "请求参数：\nroleId：角色ID\norgIdList：机构ID集合")RequestRoleAuthVO requestRoleAuthVO) {
        if(null == requestRoleAuthVO.getRoleId() || CollectionUtils.isEmpty(requestRoleAuthVO.getOrgIdList())){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("角色ID或机构ID不能为空!");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            roleAuthService.updateRoleOrgAuth(requestRoleAuthVO.getRoleId(), requestRoleAuthVO.getOrgIdList(), userSession.getArchiveId());
            logger.info("updateRoleOrgAuth success！roleId={},orgIdList={},operatorId={}", requestRoleAuthVO.getRoleId(), requestRoleAuthVO.getOrgIdList(),userSession.getArchiveId());
            responseResult = ResponseResult.SUCCESS();

        }catch (Exception e){
            logger.info("updateRole exception！roleId={},orgIdList={},exception={}", requestRoleAuthVO.getRoleId(), requestRoleAuthVO.getOrgIdList(), e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("修改角色机构权限异常！");
        }
        return responseResult;
    }


    @ApiOperation(value="查询当前登录企业档案自定义表列表", notes="查询当前登录企业档案自定义表列表")
    @RequestMapping(value = "/searchCustomArchiveTableList",method = RequestMethod.POST)
    public ResponseResult<CustomArchiveTableFieldVO> searchCustomArchiveTableList() {
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            List<CustomArchiveTableFieldVO> customArchiveTableList = roleAuthService.searchCustomArchiveTableList(userSession.getCompanyId());
            logger.info("searchCustomArchiveTableList success！companyId={}", userSession.getCompanyId());
            responseResult = ResponseResult.SUCCESS();
            responseResult.setResult(customArchiveTableList);
        }catch (Exception e){
            logger.info("searchCustomArchiveTableList exception！exception={}", e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据企业ID查询角色自定义表列表异常！");
        }
        return responseResult;
    }


    @ApiOperation(value="根据自定义表ID查询自定义字段列表", notes="根据字段义表ID查询自定义字段列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "tableId", value = "自定义表ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/searchCustomArchiveTableFieldListByTableId",method = RequestMethod.POST)
    public ResponseResult<CustomArchiveTableFieldVO> searchCustomArchiveTableFieldListByTableId(Integer roleId,Integer tableId) {
        if(null == tableId){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("表ID不能为空!");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            List<CustomArchiveTableFieldVO> customArchiveTableList = roleAuthService.searchCustomArchiveTableFieldListByTableId(roleId,tableId);
            logger.info("searchCustomArchiveTableFieldListByTableId success！tableId={}", tableId);
            responseResult = ResponseResult.SUCCESS();
            responseResult.setResult(customArchiveTableList);
        }catch (Exception e){
            logger.info("searchCustomArchiveTableFieldListByTableId exception！tableId={},exception={}", tableId, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据自定义表ID查询自定义字段列表异常！");
        }
        return responseResult;
    }


    @ApiOperation(value="根据角色ID查询角色自定义表字段列表", notes="根据角色ID查询角色自定义表字段列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/searchCustomArchiveTableFieldListByRoleId",method = RequestMethod.POST)
    public ResponseResult<CustomArchiveTableFieldVO> searchCustomArchiveTableFieldListByRoleId(Integer roleId) {
        if(null == roleId){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("角色ID不能为空!");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            List<CustomArchiveTableFieldVO> customArchiveTableList = roleAuthService.searchCustomArchiveTableFieldListByRoleId(roleId);
            logger.info("searchCustomArchiveTableFieldListByRoleId success！roleId={}", roleId);
            responseResult = ResponseResult.SUCCESS();
            responseResult.setResult(customArchiveTableList);
        }catch (Exception e){
            logger.info("searchCustomArchiveTableFieldListByRoleId exception！roleId={},exception={}", roleId, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据角色ID查询角色自定义表字段列表异常！");
        }
        return responseResult;
    }


    @ApiOperation(value="修改角色自定义人员表字段权限", notes="参数只需roleId、fieldId、readWriteCode")
    @RequestMapping(value = "/updateRoleCustomArchiveTableFieldAuth",method = RequestMethod.POST)
    public ResponseResult updateRoleCustomArchiveTableFieldAuth(@RequestBody List<CustomArchiveTableFieldVO> customTableFieldList) {
        if(CollectionUtils.isEmpty(customTableFieldList)){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("请求对象不能为空!");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            int resultNumber = roleAuthService.updateRoleCustomArchiveTableFieldAuth(customTableFieldList, userSession.getArchiveId());
            if(resultNumber > 0){
                logger.info("updateRoleCustomArchiveTableFieldAuth success！customTableFieldList={},operatorId={}", customTableFieldList, userSession.getArchiveId());
                responseResult = ResponseResult.SUCCESS();
            }else{
                logger.info("updateRoleCustomArchiveTableFieldAuth fail！customTableFieldList={},operatorId={}", customTableFieldList, userSession.getArchiveId());
                responseResult = ResponseResult.FAIL();
            }
        }catch (Exception e){
            logger.info("updateRoleCustomArchiveTableFieldAuth exception！customTableFieldList={},exception={}", customTableFieldList, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("修改角色自定义人员表字段权限异常！");
        }
        return responseResult;
    }


    @ApiOperation(value="保存数据级权限定义", notes="保存数据级权限定义")
    @RequestMapping(value = "/saveRoleDataLevelAuth",method = RequestMethod.POST)
    public ResponseResult saveRoleDataLevelAuth(RoleDataLevelAuthVO roleDataLevelAuthVO) {

        if(null == roleDataLevelAuthVO || null == roleDataLevelAuthVO.getRoleId() || CollectionUtils.isEmpty(roleDataLevelAuthVO.getChildDataLevelAuthList())){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("角色ID或数据组权限不能为空!");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            roleDataLevelAuthVO.setOperatorId(userSession.getArchiveId());
            int resultNumber = roleAuthService.saveRoleDataLevelAuth(roleDataLevelAuthVO);
            if(resultNumber > 0){
                logger.info("saveRoleDataLevelAuth success！roleId={},childDataLevelAuthList={},operatorId={}", roleDataLevelAuthVO.getRoleId(), roleDataLevelAuthVO.getChildDataLevelAuthList(), userSession.getArchiveId());
                responseResult = ResponseResult.SUCCESS();
            }else{
                logger.info("saveRoleDataLevelAuth fail！roleId={},childDataLevelAuthList={},operatorId={}", roleDataLevelAuthVO.getRoleId(), roleDataLevelAuthVO.getChildDataLevelAuthList(), userSession.getArchiveId());
                responseResult = ResponseResult.FAIL();
            }
        }catch (Exception e){
            logger.info("saveRoleDataLevelAuth exception！roleId={},childDataLevelAuthList={},exception={}", roleDataLevelAuthVO.getRoleId(), roleDataLevelAuthVO.getChildDataLevelAuthList(), e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("保存数据级权限定义异常！");
        }
        return responseResult;
    }

}
