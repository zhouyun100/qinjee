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
import com.qinjee.masterdata.model.vo.auth.MenuVO;
import com.qinjee.masterdata.model.vo.auth.OrganizationVO;
import com.qinjee.masterdata.service.auth.AuthHandoverService;
import com.qinjee.model.response.ResponseResult;
import com.qinjee.utils.DateFormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    private AuthHandoverService authHandoverService;

    @ApiOperation(value="查询角色功能权限树", notes="根据角色ID查询菜单功能权限树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/searchRoleAuthTree",method = RequestMethod.GET)
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
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/searchOrgAuthTree",method = RequestMethod.GET)
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
            List<OrganizationVO> organizationList = authHandoverService.searchOrgAuthTree(userSession.getArchiveId(), roleId);
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "archiveId", value = "档案ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "roleIdList", value = "角色ID集合", required = true, dataType = "int", allowMultiple = true)
    })
    @RequestMapping(value = "/roleRecoveryByArchiveId",method = RequestMethod.GET)
    public ResponseResult roleRecoveryByArchiveId(Integer archiveId,@RequestParam List<Integer> roleIdList) {
        if(null == archiveId || CollectionUtils.isEmpty(roleIdList)){
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
            int resultNumber = authHandoverService.roleRecoveryByArchiveId(archiveId,roleIdList,userSession.getArchiveId());
            if(resultNumber > 0){
                logger.info("roleRecoveryByArchiveId success！archiveId={},roleIdList={}", archiveId, roleIdList);
                responseResult = ResponseResult.SUCCESS();
            }else{
                logger.info("roleRecoveryByArchiveId fail！archiveId={},roleIdList={}", archiveId, roleIdList);
                responseResult = ResponseResult.FAIL();
            }

        }catch (Exception e){
            logger.info("roleRecoveryByArchiveId exception！archiveId={},roleIdList={},exception={}", archiveId, roleIdList, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据档案回收角色异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="机构回收", notes="根据档案回收机构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "archiveId", value = "档案ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "orgIdList", value = "机构ID集合", required = true, dataType = "int", allowMultiple = true)
    })
    @RequestMapping(value = "/orgRecoveryByArchiveId",method = RequestMethod.GET)
    public ResponseResult orgRecoveryByArchiveId(Integer archiveId,@RequestParam List<Integer> orgIdList) {
        if(null == archiveId || CollectionUtils.isEmpty(orgIdList)){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("档案或机构不能为空!");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            int resultNumber = authHandoverService.orgRecoveryByArchiveId(archiveId,orgIdList,userSession.getArchiveId());
            if(resultNumber > 0){
                logger.info("orgRecoveryByArchiveId success！archiveId={},orgIdList={}", archiveId, orgIdList);
                responseResult = ResponseResult.SUCCESS();
            }else{
                logger.info("orgRecoveryByArchiveId fail！archiveId={},orgIdList={}", archiveId, orgIdList);
                responseResult = ResponseResult.FAIL();
            }
        }catch (Exception e){
            logger.info("orgRecoveryByArchiveId exception！archiveId={},orgIdList={},exception={}", archiveId, orgIdList, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据档案回收机构异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="角色移交", notes="根据档案移交角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "handoverArchiveId", value = "移交人档案ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "acceptArchiveId", value = "接收人档案ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "roleIdList", value = "角色ID集合", required = true, dataType = "int", allowMultiple = true)
    })
    @RequestMapping(value = "/roleHandoverByArchiveId",method = RequestMethod.GET)
    public ResponseResult roleHandoverByArchiveId(Integer handoverArchiveId, Integer acceptArchiveId, @RequestParam List<Integer> roleIdList) {
        if(null == handoverArchiveId || null == acceptArchiveId ||CollectionUtils.isEmpty(roleIdList)){
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
            int resultNumber = authHandoverService.roleHandoverByArchiveId(handoverArchiveId,acceptArchiveId,roleIdList,userSession.getArchiveId());
            if(resultNumber > 0){
                logger.info("roleHandoverByArchiveId success！handoverArchiveId={},acceptArchiveId={},roleIdList={}", handoverArchiveId, acceptArchiveId, roleIdList);
                responseResult = ResponseResult.SUCCESS();
            }else{
                logger.info("roleHandoverByArchiveId fail！handoverArchiveId={},acceptArchiveId={},roleIdList={}", handoverArchiveId, acceptArchiveId, roleIdList);
                responseResult = ResponseResult.FAIL();
            }
        }catch (Exception e){
            logger.info("roleHandoverByArchiveId exception！handoverArchiveId={},acceptArchiveId={},roleIdList={},exception={}", handoverArchiveId, acceptArchiveId, roleIdList, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据档案移交角色异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="机构移交", notes="根据档案移交机构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "handoverArchiveId", value = "移交人档案ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "acceptArchiveId", value = "接收人档案ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "orgIdList", value = "机构ID集合", required = true, dataType = "int", allowMultiple = true)
    })
    @RequestMapping(value = "/orgHandoverByArchiveId",method = RequestMethod.GET)
    public ResponseResult orgHandoverByArchiveId(Integer handoverArchiveId, Integer acceptArchiveId, @RequestParam List<Integer> orgIdList) {
        if(null == handoverArchiveId || null == acceptArchiveId ||CollectionUtils.isEmpty(orgIdList)){
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
            int resultNumber = authHandoverService.orgHandoverByArchiveId(handoverArchiveId,acceptArchiveId,orgIdList,userSession.getArchiveId());
            if(resultNumber > 0){
                logger.info("orgHandoverByArchiveId success！handoverArchiveId={},acceptArchiveId={},orgIdList={}", handoverArchiveId, acceptArchiveId, orgIdList);
                responseResult = ResponseResult.SUCCESS();
            }else{
                logger.info("orgHandoverByArchiveId fail！handoverArchiveId={},acceptArchiveId={},orgIdList={}", handoverArchiveId, acceptArchiveId, orgIdList);
                responseResult = ResponseResult.FAIL();
            }
        }catch (Exception e){
            logger.info("orgHandoverByArchiveId exception！handoverArchiveId={},acceptArchiveId={},orgIdList={},exception={}", handoverArchiveId, acceptArchiveId, orgIdList, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据档案移交机构异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="角色托管", notes="根据档案托管角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "trusteeshipArchiveId", value = "托管人档案ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "acceptArchiveId", value = "接收人档案ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "trusteeshipBeginTime", value = "托管开始时间", required = true, dataType = "String"),
            @ApiImplicitParam(name = "trusteeshipEndTime", value = "托管结束时间", required = true, dataType = "String"),
            @ApiImplicitParam(name = "roleIdList", value = "角色ID集合", required = true, dataType = "int", allowMultiple = true)
    })
    @RequestMapping(value = "/roleTrusteeshipByArchiveId",method = RequestMethod.GET)
    public ResponseResult roleTrusteeshipByArchiveId(Integer trusteeshipArchiveId, Integer acceptArchiveId, String trusteeshipBeginTime, String trusteeshipEndTime, @RequestParam List<Integer> roleIdList) {
        if(null == trusteeshipArchiveId || null == acceptArchiveId || CollectionUtils.isEmpty(roleIdList)){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("移交人或接收人或角色不能为空!");
            return responseResult;
        }

        Date beginTime = DateFormatUtil.formatStrToDate(trusteeshipBeginTime,DateFormatUtil.DATE_FORMAT);
        Date endTime = DateFormatUtil.formatStrToDate(trusteeshipEndTime,DateFormatUtil.DATE_FORMAT);
        if(null == beginTime || null == endTime){
            logger.info("roleTrusteeshipByArchiveId exception！trusteeshipBeginTime={},trusteeshipEndTime={}", trusteeshipBeginTime,trusteeshipEndTime);
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
            int resultNumber = authHandoverService.roleTrusteeshipByArchiveId(trusteeshipArchiveId,acceptArchiveId,beginTime,endTime,roleIdList,userSession.getArchiveId());
            if(resultNumber > 0){
                logger.info("roleTrusteeshipByArchiveId success！trusteeshipArchiveId={},acceptArchiveId={},trusteeshipBeginTime={},trusteeshipEndTime={},roleIdList={}", trusteeshipArchiveId,acceptArchiveId,trusteeshipBeginTime,trusteeshipEndTime,roleIdList);
                responseResult = ResponseResult.SUCCESS();
            }else{
                logger.info("roleTrusteeshipByArchiveId fail！trusteeshipArchiveId={},acceptArchiveId={},trusteeshipBeginTime={},trusteeshipEndTime={},roleIdList={}", trusteeshipArchiveId,acceptArchiveId,trusteeshipBeginTime,trusteeshipEndTime,roleIdList);
                responseResult = ResponseResult.FAIL();
            }
        }catch (Exception e){
            logger.info("roleTrusteeshipByArchiveId exception！trusteeshipArchiveId={},acceptArchiveId={},trusteeshipBeginTime={},trusteeshipEndTime={},roleIdList,exception={}", trusteeshipArchiveId,acceptArchiveId,trusteeshipBeginTime,trusteeshipEndTime,roleIdList, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据档案托管角色异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="机构托管", notes="根据档案移交机构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "trusteeshipArchiveId", value = "托管人档案ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "acceptArchiveId", value = "接收人档案ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "trusteeshipBeginTime", value = "托管开始时间", required = true, dataType = "String"),
            @ApiImplicitParam(name = "trusteeshipEndTime", value = "托管结束时间", required = true, dataType = "String"),
            @ApiImplicitParam(name = "orgIdList", value = "机构ID集合", required = true, dataType = "int", allowMultiple = true)
    })
    @RequestMapping(value = "/orgTrusteeshipByArchiveId",method = RequestMethod.GET)
    public ResponseResult orgTrusteeshipByArchiveId(Integer trusteeshipArchiveId, Integer acceptArchiveId, String trusteeshipBeginTime, String trusteeshipEndTime,@RequestParam List<Integer> orgIdList) {
        if(null == trusteeshipArchiveId || null == acceptArchiveId ||CollectionUtils.isEmpty(orgIdList)){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("移交人或接收人或角色不能为空!");
            return responseResult;
        }

        Date beginTime = DateFormatUtil.formatStrToDate(trusteeshipBeginTime,DateFormatUtil.DATE_FORMAT);
        Date endTime = DateFormatUtil.formatStrToDate(trusteeshipEndTime,DateFormatUtil.DATE_FORMAT);
        if(null == beginTime || null == endTime){
            logger.info("orgTrusteeshipByArchiveId exception！trusteeshipBeginTime={},trusteeshipEndTime={}", trusteeshipBeginTime,trusteeshipEndTime);
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
            int resultNumber = authHandoverService.orgTrusteeshipByArchiveId(trusteeshipArchiveId,acceptArchiveId,beginTime,endTime,orgIdList,userSession.getArchiveId());
            if(resultNumber > 0){
                logger.info("orgTrusteeshipByArchiveId success！trusteeshipArchiveId={},acceptArchiveId={},trusteeshipBeginTime={},trusteeshipEndTime={},orgIdList={}", trusteeshipArchiveId,acceptArchiveId,trusteeshipBeginTime,trusteeshipEndTime,orgIdList);
                responseResult = ResponseResult.SUCCESS();
            }else{
                logger.info("orgTrusteeshipByArchiveId fail！trusteeshipArchiveId={},acceptArchiveId={},trusteeshipBeginTime={},trusteeshipEndTime={},orgIdList={}", trusteeshipArchiveId,acceptArchiveId,trusteeshipBeginTime,trusteeshipEndTime,orgIdList);
                responseResult = ResponseResult.FAIL();
            }

        }catch (Exception e){
            logger.info("orgTrusteeshipByArchiveId exception！trusteeshipArchiveId={},acceptArchiveId={},trusteeshipBeginTime={},trusteeshipEndTime={},orgIdList,exception={}", trusteeshipArchiveId,acceptArchiveId,trusteeshipBeginTime,trusteeshipEndTime,orgIdList, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据档案托管角色异常！");
        }
        return responseResult;
    }
}
