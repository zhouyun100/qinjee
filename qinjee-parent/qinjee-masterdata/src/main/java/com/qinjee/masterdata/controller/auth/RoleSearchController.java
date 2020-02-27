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
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.entity.Role;
import com.qinjee.masterdata.model.vo.auth.*;
import com.qinjee.masterdata.service.auth.RoleSearchService;
import com.qinjee.masterdata.service.organation.OrganizationService;
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
@Api(tags = "【权限管理】角色反查接口")
@RestController
@CrossOrigin
@RequestMapping("/roleSearch")
public class RoleSearchController extends BaseController{

    private static Logger logger = LogManager.getLogger(RoleSearchController.class);

    private ResponseResult responseResult;

    @Autowired
    private RoleSearchService roleSearchService;

    @Autowired
    private OrganizationService organizationService;

    @GetMapping("/getOrganizationTree")
    @ApiOperation(value = "根据是否封存查询当前登录用户下所有的机构树",notes = "根据是否封存查询当前登录用户下所有的机构树")
    public ResponseResult<List<OrganizationVO>> getOrganizationTree(@RequestParam("isEnable") @ApiParam(value = "是否含有封存 0不含有、1含有",example = "0") Short isEnable){
        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }
            if (isEnable != null && isEnable == 0) {
                isEnable = null;
            }else{
                isEnable = 0;
            }
            //还需要查托管的机构
            List<OrganizationVO> organizationList = organizationService.getAllOrganizationTree(userSession.getArchiveId(),isEnable);
            logger.info("getOrganizationTree success！isEnable={},archiveId={}",isEnable,userSession.getArchiveId());
            responseResult = ResponseResult.SUCCESS();
            responseResult.setResult(organizationList);
        }catch (Exception e){
            logger.info("getOrganizationTree exception!isEnable={},exception={}", isEnable,e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据是否封存查询当前登录用户下所有的机构树异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="根据机构ID、工号或姓名模糊查询员工列表", notes="根据工号或姓名模糊查询员工列表")
    @RequestMapping(value = "/searchArchiveListByUserName",method = RequestMethod.POST)
    public ResponseResult<PageResult<ArchiveInfoVO>> searchArchiveListByUserName(
            @RequestBody @ApiParam(value = "请求参数：\ncurrentPage：当前页数\npageSize：总页数(不传默认查全部)\norgId：机构ID\nuserName：姓名或工号")RequestArchivePageVO archivePageVO) {

        try{
            userSession = getUserSession();
            if(userSession == null){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("Session失效！");
                return responseResult;
            }else{
                archivePageVO.setCompanyId(userSession.getCompanyId());
                PageResult<ArchiveInfoVO> pageResult = roleSearchService.searchArchiveListByUserName(archivePageVO,userSession.getArchiveId());

                logger.info("searchArchiveListByUserName success！userName={},companyId={}",archivePageVO.getUserName(),userSession.getCompanyId());
                responseResult = ResponseResult.SUCCESS();
                responseResult.setResult(pageResult);
            }
        }catch (Exception e){
            logger.info("searchArchiveListByUserName exception!userName={},exception={}", archivePageVO.getUserName(),e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据工号或姓名模糊查询员工列表异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="根据档案ID查询角色树", notes="根据档案ID查询角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "archiveId", value = "档案ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "/searchRoleTreeByArchiveId",method = RequestMethod.POST)
    public ResponseResult<RoleGroupVO> searchRoleListByArchiveId(Integer archiveId) {
        if(null == archiveId){
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
            List<RoleGroupVO> userRoleList = roleSearchService.searchRoleTreeByArchiveId(archiveId,userSession.getCompanyId());
            responseResult = ResponseResult.SUCCESS();
            responseResult.setResult(userRoleList);
            logger.info("searchRoleListByArchiveId success！archiveId={}", archiveId);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("searchRoleListByArchiveId exception!archiveId={},exception={}", archiveId, e.toString());
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("根据档案ID查询角色列表异常！");
        }
        return responseResult;
    }


    @ApiOperation(value="修改用户角色", notes="修改用户角色")
    @RequestMapping(value = "/updateArchiveRole",method = RequestMethod.POST)
    public ResponseResult updateArchiveRole(@RequestBody RequestArchiveRoleVO requestArchiveRoleVO) {
        if(null == requestArchiveRoleVO.getArchiveId()){
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
            roleSearchService.updateArchiveRole(requestArchiveRoleVO.getArchiveId(), requestArchiveRoleVO.getRoleIdList(), userSession.getArchiveId());

            logger.info("updateArchiveRole success! archiveId={};roleIdList={};", requestArchiveRoleVO.getArchiveId(), requestArchiveRoleVO.getRoleIdList());
            responseResult = ResponseResult.SUCCESS();
        }catch (Exception e){
            logger.info("updateArchiveRole exception! archiveId={};roleIdList={};exception={}", requestArchiveRoleVO.getArchiveId(), requestArchiveRoleVO.getRoleIdList(), e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("修改用户角色异常！");
        }
        return responseResult;
    }
}
