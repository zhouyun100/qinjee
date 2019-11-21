package com.qinjee.masterdata.controller.organization;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.vo.organization.OrganizationPageVo;
import com.qinjee.masterdata.model.vo.organization.OrganizationVoo;
import com.qinjee.masterdata.service.organation.OrganizationService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description 机构controller
 * @createTime 2019年09月10日 10:19:00
 */
@RequestMapping("/organization")
@RestController
@Api(tags = "【机构管理】机构接口")
public class OrganizationController extends BaseController {
    private static Logger logger = LogManager.getLogger(OrganizationController.class);
    @Autowired
    private OrganizationService organizationService;
    //测试提交

    @GetMapping("/getOrganizationTree")
    @ApiOperation(value = "根据是否封存查询用户下所有的机构,树形结构展示",notes = "高雄")
    public ResponseResult<PageResult<OrganizationVO>> getOrganizationTree(@RequestParam("isEnable") @ApiParam(value = "是否含有封存 0不含有、1含有",example = "0") Short isEnable){
        UserSession userSession = getUserSession();
        //还需要查托管的机构
        PageResult<OrganizationVO> pageResult = organizationService.getOrganizationPageTree(userSession,isEnable);
        return new ResponseResult<>(pageResult);
    }

    @GetMapping("/getOrganizationGraphics")
    @ApiOperation(value = "根据是否封存查询用户下所有的机构,图形化展示",notes = "高雄")
    public ResponseResult<PageResult<OrganizationVO>> getOrganizationGraphics(@RequestParam("isEnable") @ApiParam(value = "是否含有封存 0不含有、1含有",example = "0") Short isEnable,
                                                                              @RequestParam("orgId") @ApiParam(value = "机构ID",example = "0") Integer orgId){
        PageResult<OrganizationVO> pageResult = organizationService.getOrganizationGraphics(getUserSession(), isEnable, orgId);
        return new ResponseResult<>(pageResult);
    }

    /** 
    * @Description: 查询用户下所有机构及子机构 
    * @Param:  
    * @return:  
    * @Author: penghs 
    * @Date: 2019/11/20 0020 
    */
    @ApiOperation(value = "查询用户下所有的机构及子机构,树形展示",notes = "彭洪思")
    @GetMapping("/getAllOrganizationTree")
    public ResponseResult<List<OrganizationVO>> getAllOrganizationTree(Short isEnable){
        UserSession userSession = getUserSession();
        List<OrganizationVO> organizationVOList =organizationService.getAllOrganizationTree(userSession, isEnable);
        return new ResponseResult(organizationVOList);
    }


    @PostMapping("/getOrganizationPageList")
    @ApiOperation(value = "根据条件分页查询用户下所有的机构",notes = "高雄")
    //TODO
    public ResponseResult<PageResult<OrganizationVO>> getOrganizationPageList(@RequestBody(required = false) OrganizationPageVo organizationPageVo){
        UserSession userSession = getUserSession();
        PageResult<OrganizationVO> pageResult = organizationService.getOrganizationList(organizationPageVo,userSession);
        return new ResponseResult<>(pageResult);
    }




    @PostMapping("/addOrganization")
    @ApiOperation(value = "新增机构",notes = "高雄")
    public ResponseResult addOrganization(@RequestBody OrganizationVoo organizationVo){
        ResponseResult responseResult ;
        try {
            responseResult=organizationService.addOrganization(getUserSession(), organizationVo);
            return responseResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @PostMapping("/editOrganization")
    @ApiOperation(value = "编辑机构",notes = "高雄")
    public ResponseResult editOrganization(@RequestBody OrganizationVoo organizationVo){
        return organizationService.editOrganization(organizationVo);
    }

    //TODO 递归与循环中涉及多次数据库操作与对象的创建，待优化
    @PostMapping("/deleteOrganizationById")
    @ApiOperation(value = "删除机构",notes = "高雄")
    public ResponseResult deleteOrganizationById(@RequestBody List<Integer> orgIds){
        return organizationService.deleteOrganizationById(orgIds);
    }

    @GetMapping("/sealOrganizationByIds")
    @ApiOperation(value = "封存/封存机构", notes = "高雄")
    public ResponseResult sealOrganizationByIds(@RequestParam("orgIds") @ApiParam(value = "机构Id",example = "1",allowMultiple = true, required = true) List<Integer> orgIds,
                                             @RequestParam("isEnable") @ApiParam(value = "0 封存、1 解封",example = "0") Short isEnable){
        return organizationService.sealOrganizationByIds(orgIds, isEnable);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "newOrgName", value = "新机构名称", paramType = "query", dataType = "String", required = true, example = "新机构名称"),
            @ApiImplicitParam(name = "targetOrgId", value = "归属机构Id", paramType = "query", dataType = "int" , required = true, example = "01"),
            @ApiImplicitParam(name = "orgType", value = "新机构类型", paramType = "query", dataType = "String" , required = true, example = "UNIT"),
            @ApiImplicitParam(name = "orgIds", value = "待合并机构Id", paramType = "query", dataType = "int", allowMultiple = true, required = true),
    })
    @GetMapping("/mergeOrganization")
    @ApiOperation(value = "合并机构", notes = "高雄")
    public ResponseResult mergeOrganization(@RequestParam("newOrgName") String newOrgName,
                                            @RequestParam("targetOrgId") Integer targetOrgId,
                                            @RequestParam("orgType") String orgType,
                                            @RequestParam("orgIds") List<Integer> orgIds){
        UserSession userSession = getUserSession();
        return organizationService.mergeOrganization(newOrgName, targetOrgId, orgType, orgIds, userSession);
    }

    @GetMapping("/getUserArchiveListByUserName")
    @ApiOperation(value = "机构负责人查询")
    public ResponseResult<PageResult<UserArchive>> getUserArchiveListByUserName(@ApiParam(value = "姓名", example = "张三", required = true) String userName){
        return organizationService.getUserArchiveListByUserName(userName);
    }

    /**
     * @Description:
     *  AorgId:2                                        AorgId：1
     *  BorgId:1    传参  在后端进行排序id更新-----》   BorgId：2
     *  CorgId:3                                        CorgId：3
     * @Param: [preOrgId, midOrgId, nextOrgId]
     * @return: com.qinjee.model.response.ResponseResult
     * @Author: penghs
     * @Date: 2019/11/20 0020
     */
    @GetMapping("/sortOrganization")
    @ApiOperation(value = "机构排序", notes = "彭洪思")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orgIds", value = "需要排序的机构编码", paramType = "query", dataType = "int", allowMultiple = true, required = true)
    })
    public ResponseResult sortOrganizationInOrg(@RequestParam LinkedList<String> orgIds){
        ResponseResult responseResult = organizationService.sortOrganization(orgIds);
        return responseResult;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "orgCodes", value = "需要合并机构的编码", paramType = "query", dataType = "int", allowMultiple = true, required = true),
            @ApiImplicitParam(name = "targetOrgCode", value = "目标机构Id", paramType = "query", dataType = "int", required = true, example = "0101"),
    })
    @GetMapping("/transferOrganization")
    @ApiOperation(value = "划转机构", notes = "高雄")
    public ResponseResult transferOrganization(@RequestParam("orgIds") List<Integer> orgIds,
                                             @RequestParam("targetOrgId") Integer targetOrgId){
        return organizationService.transferOrganization(orgIds, targetOrgId);
    }

    @ApiOperation(value = "下载模板", notes = "高雄")
    @GetMapping("/downloadTemplate")
    public ResponseResult downloadTemplateInOrg(HttpServletResponse response){
        return organizationService.downloadTemplate(response);
    }


    @ApiOperation(value = "根据查询条件导出Excel", notes = "高雄")
    @PostMapping("/downloadExcelByCondition")
    public ResponseResult downloadExcelByConditionFromOrg(@RequestBody OrganizationPageVo organizationPageVo, HttpServletResponse response){
        return organizationService.downloadExcelByCondition(organizationPageVo, response, getUserSession());
    }

    @ApiOperation(value = "根据选择的机构id导出Excel", notes = "彭洪思")
    @GetMapping("/downloadOrganizationToExcelByOrgId")
    public ResponseResult downloadOrganizationToExcelByOrgId(@ApiParam(value = "导出路径",required = true)@RequestParam("filePath") String filePath,@RequestParam("orgIds") @ApiParam(value = "所选机构的id",required = true) List<Integer> orgIds, HttpServletResponse response){
        return organizationService.downloadOrganizationToExcelByOrgId(filePath,orgIds, response, getUserSession());
    }
    @ApiOperation(value = "导出用户下所有机构到Excel", notes = "彭洪思")
    @GetMapping("/downloadAllOrganizationToExcel")
    public ResponseResult downloadAllOrganizationToExcel(@ApiParam(value = "导出路径",required = true)@RequestParam("filePath") String filePath, HttpServletResponse response){
        return organizationService.downloadAllOrganizationToExcel(filePath,response, getUserSession());
    }

    @ApiOperation(value = "导入机构Excel", notes = "高雄")
    @PostMapping("/uploadExcel")
    public ResponseResult uploadExcelInOrg(@ApiParam(value = "需要导入的Excel文件", required = true) MultipartFile file){
        return organizationService.uploadExcel(file, getUserSession());
    }

    @ApiOperation(value = "岗位维护机构岗位树状图展示", notes = "高雄")
    @GetMapping("/getOrganizationPositionTree")
    public ResponseResult<List<OrganizationVO>> getOrganizationPositionTree(@ApiParam(value = "是否含有封存 0不含有、1含有",example = "0") Short isEnable){
        return organizationService.getOrganizationPositionTree(getUserSession(), isEnable);
    }


}
