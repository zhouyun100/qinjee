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
import com.qinjee.masterdata.model.vo.auth.ArchiveInfoVO;
import com.qinjee.masterdata.model.vo.auth.RequestArchivePageVO;
import com.qinjee.masterdata.model.vo.auth.RequestRoleArchiveVO;
import com.qinjee.masterdata.model.vo.auth.RoleGroupVO;
import com.qinjee.masterdata.service.auth.ArchiveAuthService;
import com.qinjee.masterdata.service.auth.RoleSearchService;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
}
