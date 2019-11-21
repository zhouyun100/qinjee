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
import com.qinjee.masterdata.model.entity.Role;
import com.qinjee.masterdata.model.vo.auth.*;
import com.qinjee.masterdata.service.auth.AuthHandoverService;
import com.qinjee.masterdata.service.auth.RoleSearchService;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import com.qinjee.utils.DateFormatUtil;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author 周赟
 * @date 2019/9/10
 */
@Api(tags = "【权限管理】权限移交接口")
@RestController
@RequestMapping("/authHandover")
public class AuthHandoverController extends BaseController{

    private static Logger logger = LogManager.getLogger(AuthHandoverController.class);

    private ResponseResult responseResult;

    @Autowired
    private RoleSearchService roleSearchService;

    @Autowired
    private AuthHandoverService authHandoverService;

    @ApiOperation(value="查询角色功能权限树", notes="根据角色ID查询菜单功能权限树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/searchRoleAuthTree",method = RequestMethod.POST)
    public ResponseResult<MenuVO> searchRoleAuthTreeInOrg(Integer roleId) {
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
            List<MenuVO> menuList = authHandoverService.searchRoleAuthTree(userSession.getArchiveId(), roleId, userSession.getCompanyId());
            logger.info("searchRoleAuthTree success！roleId={}", roleId);
            responseResult = ResponseResult.SUCCESS();
            responseResult.setResult(menuList);
        }catch (Exception e){
            logger.info("searchRoleAuthTree exception！roleId={},exception={}", roleId, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据角色ID查询菜单功能权限树异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="查询角色机构权限树", notes="根据角色ID查询机构权限树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "archiveId", value = "档案ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/searchOrgAuthTree",method = RequestMethod.POST)
    public ResponseResult<OrganizationVO> searchOrgAuthTreeInOrg(Integer roleId,Integer archiveId) {
        if(null == roleId || null == archiveId){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("角色和档案ID不能为空!");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            List<OrganizationVO> organizationList = authHandoverService.searchOrgAuthTree(userSession.getArchiveId(), roleId, archiveId);
            logger.info("searchOrgAuthTree success！roleId={}", roleId);
            responseResult = ResponseResult.SUCCESS();
            responseResult.setResult(organizationList);
        }catch (Exception e){
            logger.info("searchOrgAuthTree exception！roleId={},exception={}", roleId, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据角色ID查询机构权限树异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="角色回收", notes="根据档案回收角色")
    @RequestMapping(value = "/roleRecoveryByArchiveId",method = RequestMethod.POST)
    public ResponseResult roleRecoveryByArchiveId(@RequestBody @ApiParam(value = "请求参数：\narchiveId：档案ID\nroleIdList：角色ID集合") RequestAuthHandoverVO requestAuthHandoverVO) {
        if(null == requestAuthHandoverVO.getArchiveId() || CollectionUtils.isEmpty(requestAuthHandoverVO.getRoleIdList())){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("档案或角色不能为空!");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            int resultNumber = authHandoverService.roleRecoveryByArchiveId(requestAuthHandoverVO.getArchiveId(),requestAuthHandoverVO.getRoleIdList(),userSession.getArchiveId());
            if(resultNumber > 0){
                logger.info("roleRecoveryByArchiveId success！archiveId={},roleIdList={}", requestAuthHandoverVO.getArchiveId(), requestAuthHandoverVO.getRoleIdList());
                responseResult = ResponseResult.SUCCESS();
            }else{
                logger.info("roleRecoveryByArchiveId fail！archiveId={},roleIdList={}", requestAuthHandoverVO.getArchiveId(), requestAuthHandoverVO.getRoleIdList());
                responseResult = ResponseResult.FAIL();
            }

        }catch (Exception e){
            logger.info("roleRecoveryByArchiveId exception！archiveId={},roleIdList={},exception={}", requestAuthHandoverVO.getArchiveId(), requestAuthHandoverVO.getRoleIdList(), e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据档案回收角色异常！");
        }
        return responseResult;
    }


    @ApiOperation(value="角色移交", notes="根据档案移交角色")
    @RequestMapping(value = "/roleHandoverByArchiveId",method = RequestMethod.POST)
    public ResponseResult roleHandoverByArchiveId(@RequestBody @ApiParam(value = "请求参数：\nhandoverArchiveId：移交人档案ID\nacceptArchiveId：接收人档案ID\nroleIdList：角色ID集合") RequestAuthHandoverVO requestAuthHandoverVO) {
        if(null == requestAuthHandoverVO.getHandoverArchiveId() || null == requestAuthHandoverVO.getAcceptArchiveId() ||CollectionUtils.isEmpty(requestAuthHandoverVO.getRoleIdList())){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("移交人或接收人或角色不能为空!");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            int resultNumber = authHandoverService.roleHandoverByArchiveId(requestAuthHandoverVO.getHandoverArchiveId(),requestAuthHandoverVO.getAcceptArchiveId(),requestAuthHandoverVO.getRoleIdList(),userSession.getArchiveId());
            if(resultNumber > 0){
                logger.info("roleHandoverByArchiveId success！handoverArchiveId={},acceptArchiveId={},roleIdList={}", requestAuthHandoverVO.getHandoverArchiveId(), requestAuthHandoverVO.getAcceptArchiveId(), requestAuthHandoverVO.getRoleIdList());
                responseResult = ResponseResult.SUCCESS();
            }else{
                logger.info("roleHandoverByArchiveId fail！handoverArchiveId={},acceptArchiveId={},roleIdList={}", requestAuthHandoverVO.getHandoverArchiveId(), requestAuthHandoverVO.getAcceptArchiveId(), requestAuthHandoverVO.getRoleIdList());
                responseResult = ResponseResult.FAIL();
            }
        }catch (Exception e){
            logger.info("roleHandoverByArchiveId exception！handoverArchiveId={},acceptArchiveId={},roleIdList={},exception={}", requestAuthHandoverVO.getHandoverArchiveId(), requestAuthHandoverVO.getAcceptArchiveId(), requestAuthHandoverVO.getRoleIdList(), e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据档案移交角色异常！");
        }
        return responseResult;
    }


    @ApiOperation(value="角色托管", notes="根据档案托管角色")
    @RequestMapping(value = "/roleTrusteeshipByArchiveId",method = RequestMethod.POST)
    public ResponseResult roleTrusteeshipByArchiveId(@RequestBody @ApiParam(value = "请求参数：\nacceptArchiveId：托管接收人档案ID\ntrusteeshipBeginTime：托管开始时间\ntrusteeshipEndTime：托管结束时间\nroleIdList：角色ID集合") RequestAuthHandoverVO requestAuthHandoverVO) {

        if(requestAuthHandoverVO.getAcceptArchiveId() != null && !CollectionUtils.isEmpty(requestAuthHandoverVO.getRoleIdList())){

            Date beginTime = DateFormatUtil.formatStrToDate(requestAuthHandoverVO.getTrusteeshipBeginTime(),DateFormatUtil.DATE_FORMAT);
            Date endTime = DateFormatUtil.formatStrToDate(requestAuthHandoverVO.getTrusteeshipEndTime(),DateFormatUtil.DATE_FORMAT);
            if(null == beginTime || null == endTime){
                logger.info("roleTrusteeshipByArchiveId exception！trusteeshipBeginTime={},trusteeshipEndTime={}", requestAuthHandoverVO.getTrusteeshipBeginTime(),requestAuthHandoverVO.getTrusteeshipEndTime());
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("托管起止时间为空或格式不正确!");
                return responseResult;
            }

            try{
                userSession = getUserSession();
                if(userSession == null){
                    responseResult = ResponseResult.FAIL();
                    responseResult.setMessage("Session失效！");
                    return responseResult;
                }
                int resultNumber = authHandoverService.roleTrusteeshipByArchiveId(requestAuthHandoverVO.getAcceptArchiveId(),beginTime,endTime,requestAuthHandoverVO.getRoleIdList(),userSession.getArchiveId());
                if(resultNumber > 0){
                    logger.info("roleTrusteeshipByArchiveId success！acceptArchiveId={},trusteeshipBeginTime={},trusteeshipEndTime={},roleIdList={}", requestAuthHandoverVO.getAcceptArchiveId(),requestAuthHandoverVO.getTrusteeshipBeginTime(),requestAuthHandoverVO.getTrusteeshipEndTime(),requestAuthHandoverVO.getRoleIdList());
                    responseResult = ResponseResult.SUCCESS();
                }else{
                    logger.info("roleTrusteeshipByArchiveId fail！acceptArchiveId={},trusteeshipBeginTime={},trusteeshipEndTime={},roleIdList={}", requestAuthHandoverVO.getAcceptArchiveId(),requestAuthHandoverVO.getTrusteeshipBeginTime(),requestAuthHandoverVO.getTrusteeshipEndTime(),requestAuthHandoverVO.getRoleIdList());
                    responseResult = ResponseResult.FAIL();
                }
            }catch (Exception e){
                logger.info("roleTrusteeshipByArchiveId exception！acceptArchiveId={},trusteeshipBeginTime={},trusteeshipEndTime={},roleIdList,exception={}", requestAuthHandoverVO.getAcceptArchiveId(),requestAuthHandoverVO.getTrusteeshipBeginTime(),requestAuthHandoverVO.getTrusteeshipEndTime(),requestAuthHandoverVO.getRoleIdList(), e.toString());
                e.printStackTrace();
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("根据档案托管角色异常！");
            }
        }else {
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("移交人或接收人或角色不能为空!");
            return responseResult;
        }
        return responseResult;
    }


    @ApiOperation(value="根据姓名或工号模糊查询员工列表", notes="姓名或工号至少2位字符")
    @RequestMapping(value = "/searchArchiveListByUserNameOrJobNumber",method = RequestMethod.POST)
    public ResponseResult<PageResult<ArchiveInfoVO>> searchArchiveListByUserNameOrJobNumber(
            @RequestBody @ApiParam(value = "请求参数：\ncurrentPage：当前页数\npageSize：总页数(不传默认查全部)\nuserName：姓名或工号")RequestArchivePageVO archivePageVO) {
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
            }else{
                archivePageVO.setCompanyId(userSession.getCompanyId());
                PageResult<ArchiveInfoVO> pageResult = roleSearchService.searchArchiveListByUserName(archivePageVO);

                responseResult = ResponseResult.SUCCESS();
                responseResult.setResult(pageResult);
                logger.info("searchArchiveListByUserNameOrJobNumber success！userName={},companyId={}",archivePageVO.getUserName(),userSession.getCompanyId());
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.info("searchArchiveListByUserNameOrJobNumber exception!userName={},exception={}", archivePageVO.getUserName(),e.toString());
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据工号或姓名模糊查询员工列表异常！");
        }
        return responseResult;
    }


    @ApiOperation(value="根据档案ID查询角色列表", notes="根据档案ID查询角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "archiveId", value = "档案ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/searchRoleListByArchiveId",method = RequestMethod.POST)
    public ResponseResult<Role> searchRoleListByArchiveId(Integer archiveId) {
        if(archiveId == null){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("档案ID不能为空!");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            List<UserRoleVO> userRoleList = authHandoverService.searchRoleListByArchiveId(archiveId,userSession.getCompanyId());
            logger.info("searchRoleListByArchiveId success！archiveId={}", archiveId);
            responseResult = ResponseResult.SUCCESS();
            responseResult.setResult(userRoleList);
        }catch (Exception e){
            logger.info("searchRoleListByArchiveId exception!archiveId={},exception={}", archiveId, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据档案ID查询角色列表异常！");
        }
        return responseResult;
    }
}
