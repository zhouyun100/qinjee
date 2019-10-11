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
import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.vo.auth.ArchiveInfoVO;
import com.qinjee.masterdata.model.vo.auth.RequestRoleArchivePageVO;
import com.qinjee.masterdata.model.vo.auth.RoleGroupVO;
import com.qinjee.masterdata.service.auth.ArchiveAuthService;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
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
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value="当前登录用户角色树查询", notes="当前登录用户角色树查询")
    @RequestMapping(value = "/searchRoleTree",method = RequestMethod.GET)
    public ResponseResult<RoleGroupVO> searchRoleTree() {
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

    @ApiOperation(value="根据角色ID查询员工列表", notes="根据角色ID查询员工")
    @RequestMapping(value = "/searchArchiveListByRoleId",method = RequestMethod.GET)
    public ResponseResult<PageResult<ArchiveInfoVO>> searchArchiveListByRoleId(RequestRoleArchivePageVO roleArchivePageVO) {

        try{
            PageResult<ArchiveInfoVO> archiveInfoVOList = archiveAuthService.searchArchiveListByRoleId(roleArchivePageVO);
            responseResult = ResponseResult.SUCCESS();
            responseResult.setResult(archiveInfoVOList);
        }catch (Exception e){
            logger.info("searchArchiveListByRoleId exception! roleArchivePageVO={};exception={}", roleArchivePageVO,e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据角色ID查询员工列表异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="角色新增员工", notes="角色新增员工")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "archiveIdList", value = "档案ID集合", required = true, dataType = "int", allowMultiple = true)
    })
    @RequestMapping(value = "/addArchiveRole",method = RequestMethod.GET)
    public ResponseResult addArchiveRole(Integer roleId, List<Integer> archiveIdList) {

        if(null == roleId || CollectionUtils.isEmpty(archiveIdList)){
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
            archiveAuthService.addArchiveRole(roleId,archiveIdList,userSession.getArchiveId());

            logger.info("addArchiveRole success! roleId={};archiveIdList={};", roleId, archiveIdList);
            responseResult = ResponseResult.SUCCESS();
        }catch (Exception e){
            logger.info("addArchiveRole exception! roleId={};archiveIdList={};exception={}", roleId, archiveIdList, e.toString());
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
    @RequestMapping(value = "/delArchiveRole",method = RequestMethod.GET)
    public ResponseResult delArchiveRole(Integer roleId, List<Integer> archiveIdList) {
        if(null == roleId || CollectionUtils.isEmpty(archiveIdList)){
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
            archiveAuthService.delArchiveRole(roleId,archiveIdList,userSession.getArchiveId());

            logger.info("delArchiveRole success! roleId={};archiveIdList={};", roleId, archiveIdList);
            responseResult = ResponseResult.SUCCESS();
        }catch (Exception e){
            logger.info("delArchiveRole exception! roleId={};archiveIdList={};exception={}", roleId, archiveIdList, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("角色移除员工异常！");
        }
        return responseResult;
    }
}
