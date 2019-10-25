/**
 * 文件名：RoleSearch
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
import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.vo.auth.ArchiveInfoVO;
import com.qinjee.masterdata.model.vo.auth.RequestArchivePageVO;
import com.qinjee.masterdata.model.vo.auth.UserRoleVO;
import com.qinjee.masterdata.service.auth.RoleSearchService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 周赟
 * @date 2019/9/10
 */
@Api(tags = "【权限管理】角色反查接口")
@RestController
@RequestMapping("/roleSearch")
public class RoleSearchController extends BaseController{

    private static Logger logger = LogManager.getLogger(RoleSearchController.class);

    private ResponseResult responseResult;

    @Autowired
    private RoleSearchService roleSearchService;

    @ApiOperation(value="根据工号或姓名模糊查询员工列表", notes="根据工号或姓名模糊查询员工列表")
    @RequestMapping(value = "/searchArchiveListByUserName",method = RequestMethod.POST)
    public ResponseResult<PageResult<ArchiveInfoVO>> searchArchiveListByUserName(RequestArchivePageVO archivePageVO) {
        if(archivePageVO == null || archivePageVO.getUserName() == null || archivePageVO.getUserName().length() < 2){
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

            logger.info("searchArchiveListByUserName success！userName={},companyId={}",archivePageVO.getUserName(),userSession.getCompanyId());
            responseResult = ResponseResult.SUCCESS();
            responseResult.setResult(pageResult);
        }catch (Exception e){
            logger.info("searchArchiveListByUserName exception!userName={},exception={}", archivePageVO.getUserName(),e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据工号或姓名模糊查询员工列表异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="根据档案ID查询角色列表", notes="根据档案ID查询角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "archiveId", value = "档案ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/searchRoleListByArchiveId",method = RequestMethod.GET)
    public ResponseResult<Role> searchRoleListByArchiveId(Integer archiveId) {
        if(null == archiveId){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("档案ID不能为空!");
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            List<UserRoleVO> userRoleList = roleSearchService.searchRoleListByArchiveId(archiveId,userSession.getCompanyId());
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


    @ApiOperation(value="修改用户角色", notes="修改用户角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "archiveId", value = "档案ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "roleIdList", value = "角色ID集合", required = true, dataType = "int", allowMultiple = true)
    })
    @RequestMapping(value = "/updateArchiveRole",method = RequestMethod.GET)
    public ResponseResult updateArchiveRole(Integer archiveId,@RequestParam List<Integer> roleIdList) {
        if(null == archiveId || CollectionUtils.isEmpty(roleIdList)){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("员工或角色为空！");
            return responseResult;
        }
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            roleSearchService.updateArchiveRole(archiveId, roleIdList, userSession.getArchiveId());

            logger.info("updateArchiveRole success! archiveId={};roleIdList={};", archiveId, roleIdList);
            responseResult = ResponseResult.SUCCESS();
        }catch (Exception e){
            logger.info("updateArchiveRole exception! archiveId={};roleIdList={};exception={}", archiveId, roleIdList, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("修改用户角色异常！");
        }
        return responseResult;
    }
}
