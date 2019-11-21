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
import com.qinjee.masterdata.model.vo.auth.*;
import com.qinjee.masterdata.service.auth.ArchiveAuthService;
import com.qinjee.masterdata.service.auth.RoleSearchService;
import com.qinjee.model.response.PageResult;
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
@Api(tags = "【权限管理】用户授权接口")
@RestController
@RequestMapping("/archiveAuth")
public class ArchiveAuthController extends BaseController {

    private static Logger logger = LogManager.getLogger(ArchiveAuthController.class);

    private ResponseResult responseResult;

    @Autowired
    private ArchiveAuthService archiveAuthService;

    @Autowired
    private RoleSearchService roleSearchService;

    @ApiOperation(value="当前登录用户角色树查询", notes="当前登录用户角色树查询")
    @RequestMapping(value = "/searchRoleTree",method = RequestMethod.POST)
    public ResponseResult<RoleGroupVO> searchRoleTreeInOrg() {
        userSession = getUserSession();
        if(userSession == null){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("Session失效！");
            return responseResult;
        }
        try{
            List<RoleGroupVO> roleGroupList = archiveAuthService.searchRoleTree(userSession.getCompanyId());
            responseResult = ResponseResult.SUCCESS();
            responseResult.setResult(roleGroupList);
        }catch (Exception e){
            logger.info("searchRoleTree exception! companyId={};exception={}", userSession.getCompanyId(), e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("当前登录用户角色树查询异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="根据角色ID查询员工列表", notes="根据角色ID查询员工列表")
    @RequestMapping(value = "/searchArchiveListByRoleId",method = RequestMethod.POST)
    public ResponseResult<PageResult<ArchiveInfoVO>> searchArchiveListByRoleId(@RequestBody RequestArchivePageVO archivePageVO) {
        if(archivePageVO == null || archivePageVO.getRoleId() == null){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("角色ID不能为空!");
            return responseResult;
        }else if(StringUtils.isNoneBlank(archivePageVO.getUserName()) && archivePageVO.getUserName().length() < 2){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("工号或姓名至少2位字符!");
            return responseResult;
        }

        try{
            userSession = getUserSession();

            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }else{
                archivePageVO.setCompanyId(userSession.getCompanyId());
                PageResult<ArchiveInfoVO> pageResult = roleSearchService.searchArchiveListByUserName(archivePageVO);

                responseResult = ResponseResult.SUCCESS();
                responseResult.setResult(pageResult);
                logger.info("searchArchiveListByUserName success！userName={},companyId={}",archivePageVO.getUserName(),userSession.getCompanyId());
            }

        }catch (Exception e){
            logger.info("searchArchiveListByUserName exception!userName={},exception={}", archivePageVO.getUserName(),e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据工号或姓名模糊查询员工列表异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="角色新增员工", notes="角色新增员工")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "archiveIdList", value = "档案ID集合", required = true, dataType = "int", allowMultiple = true)
    })
    @RequestMapping(value = "/addArchiveRole",method = RequestMethod.POST)
    public ResponseResult addArchiveRole(@RequestBody RequestRoleArchiveVO requestRoleArchiveVO) {

        if(null == requestRoleArchiveVO.getRoleId() || CollectionUtils.isEmpty(requestRoleArchiveVO.getArchiveIdList())){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("角色或员工为空！");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            archiveAuthService.addArchiveRole(requestRoleArchiveVO.getRoleId(),requestRoleArchiveVO.getArchiveIdList(),userSession.getArchiveId());

            logger.info("addArchiveRole success! roleId={};archiveIdList={};", requestRoleArchiveVO.getRoleId(), requestRoleArchiveVO.getArchiveIdList());
            responseResult = ResponseResult.SUCCESS();
        }catch (Exception e){
            logger.info("addArchiveRole exception! roleId={};archiveIdList={};exception={}", requestRoleArchiveVO.getRoleId(), requestRoleArchiveVO.getArchiveIdList(), e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("角色新增员工异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="角色移除员工", notes="角色移除员工")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "archiveIdList", value = "档案ID集合", required = true, dataType = "int", allowMultiple = true)
    })
    @RequestMapping(value = "/delArchiveRole",method = RequestMethod.POST)
    public ResponseResult delArchiveRole(@RequestBody RequestRoleArchiveVO requestRoleArchiveVO) {
        if(null == requestRoleArchiveVO.getRoleId() || CollectionUtils.isEmpty(requestRoleArchiveVO.getArchiveIdList())){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("角色或员工为空！");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            archiveAuthService.delArchiveRole(requestRoleArchiveVO.getRoleId(),requestRoleArchiveVO.getArchiveIdList(),userSession.getArchiveId());

            logger.info("delArchiveRole success! roleId={};archiveIdList={};", requestRoleArchiveVO.getRoleId(), requestRoleArchiveVO.getArchiveIdList());
            responseResult = ResponseResult.SUCCESS();
        }catch (Exception e){
            logger.info("delArchiveRole exception! roleId={};archiveIdList={};exception={}", requestRoleArchiveVO.getRoleId(), requestRoleArchiveVO.getArchiveIdList(), e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("角色移除员工异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="根据姓名或工号模糊查询员工列表", notes="姓名或工号至少2位字符")
    @RequestMapping(value = "/searchArchiveListByUserNameOrJobNumber",method = RequestMethod.POST)
    public ResponseResult<PageResult<ArchiveInfoVO>> searchArchiveListByUserNameOrJobNumber(@RequestBody RequestArchivePageVO archivePageVO) {
        if(StringUtils.isNoneBlank(archivePageVO.getUserName()) && archivePageVO.getUserName().length() < 2){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("工号或姓名至少2位字符!");
            return responseResult;
        }

        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            archivePageVO.setCompanyId(userSession.getCompanyId());
            PageResult<ArchiveInfoVO> pageResult = roleSearchService.searchArchiveListByUserName(archivePageVO);

            logger.info("searchArchiveListByUserNameOrJobNumber success！userName={},companyId={}",archivePageVO.getUserName(),userSession.getCompanyId());
            responseResult = ResponseResult.SUCCESS();
            responseResult.setResult(pageResult);
        }catch (Exception e){
            logger.info("searchArchiveListByUserNameOrJobNumber exception!userName={},exception={}", archivePageVO.getUserName(),e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据工号或姓名模糊查询员工列表异常！");
        }
        return responseResult;
    }


    @ApiOperation(value="获取机构档案树", notes="默认当前登录用户")
    @RequestMapping(value = "/getOrganizationArchiveTree",method = RequestMethod.POST)
    public ResponseResult<OrganizationArchiveVO> getOrganizationArchiveTree() {

        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            List<OrganizationArchiveVO> organizationArchiveList= archiveAuthService.getOrganizationArchiveTreeByArchiveId(userSession.getCompanyId(),userSession.getArchiveId());

            logger.info("getOrganizationArchiveTree success！userName={},companyId={}",userSession.getUserName(),userSession.getCompanyId());
            responseResult = ResponseResult.SUCCESS();
            responseResult.setResult(organizationArchiveList);
        }catch (Exception e){
            logger.info("getOrganizationArchiveTree exception!userName={},exception={}", userSession.getUserName(),e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("获取当前登录用户机构档案树异常！");
        }
        return responseResult;
    }


    @ApiOperation(value="根据档案ID查询机构权限树", notes="根据档案ID查询角色机构权限树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "archiveId", value = "档案ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/searchOrgAuthTreeByArchiveId",method = RequestMethod.POST)
    public ResponseResult<OrganizationVO> searchOrgAuthTree(Integer archiveId) {
        if(null == archiveId){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("员工ID不能为空!");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            List<OrganizationVO> organizationList = archiveAuthService.searchOrgAuthTree(archiveId, userSession.getArchiveId());
            if(CollectionUtils.isEmpty(organizationList)){
                logger.info("searchOrgAuthTreeByArchiveId fail！operatorId={},archiveId={},organizationList={}", userSession.getArchiveId(), archiveId, organizationList);
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("查询人员机构树结果为空！");
            }else {
                logger.info("searchOrgAuthTreeByArchiveId success！operatorId={},archiveId={},organizationList={}", userSession.getArchiveId(), archiveId, organizationList);
                responseResult = ResponseResult.SUCCESS();
                responseResult.setResult(organizationList);
            }
        }catch (Exception e){
            logger.info("searchOrgAuthTreeByArchiveId exception！archiveId={},exception={}", archiveId, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据人员ID查询机构树异常！");
        }
        return responseResult;
    }


    @ApiOperation(value="修改人员机构权限", notes="修改人员机构权限")
    @RequestMapping(value = "/updateArchiveOrgAuth",method = RequestMethod.POST)
    public ResponseResult updateArchiveOrgAuth(@RequestBody @ApiParam(value = "请求参数：\narchiveId：档案ID\norgIdList：机构ID集合")RequestRoleAuthVO requestRoleAuthVO) {
        if(null == requestRoleAuthVO.getArchiveId() || CollectionUtils.isEmpty(requestRoleAuthVO.getOrgIdList())){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("档案ID或机构ID不能为空!");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            archiveAuthService.updateArchiveOrgAuth(requestRoleAuthVO.getArchiveId(), requestRoleAuthVO.getOrgIdList(), userSession.getArchiveId());
            logger.info("updateArchiveOrgAuth success！archiveId={},orgIdList={},operatorId={}", requestRoleAuthVO.getArchiveId(), requestRoleAuthVO.getOrgIdList(),userSession.getArchiveId());
            responseResult = ResponseResult.SUCCESS();

        }catch (Exception e){
            logger.info("updateArchiveOrgAuth exception！archiveId={},orgIdList={},exception={}", requestRoleAuthVO.getArchiveId(), requestRoleAuthVO.getOrgIdList(), e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("修改人员机构权限异常！");
        }
        return responseResult;
    }
}
