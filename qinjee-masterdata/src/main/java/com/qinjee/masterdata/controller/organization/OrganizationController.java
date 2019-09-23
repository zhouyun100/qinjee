package com.qinjee.masterdata.controller.organization;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.Organization;
import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.vo.organization.OrganizationPageVo;
import com.qinjee.masterdata.model.vo.organization.OrganizationVo;
import com.qinjee.masterdata.service.organation.OrganizationService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private OrganizationService organizationService;

    @GetMapping("/getOrganizationTree")
    @ApiOperation(value = "根据是否封存查询用户下所有的机构,树形结构展示",notes = "高雄")
    public ResponseResult<PageResult<Organization>> getOrganizationTree(@RequestParam("isEnable") @ApiParam(value = "是否含有封存 0不含有、1含有",example = "0") Short isEnable){
        UserSession userSession = getUserSession();
        PageResult<Organization> pageResult = organizationService.getOrganizationTree(userSession,isEnable);
        return new ResponseResult<>(pageResult);
    }

    @GetMapping("/getOrganizationGraphics")
    @ApiOperation(value = "根据是否封存查询用户下所有的机构,图形化展示",notes = "高雄")
    public ResponseResult<PageResult<Organization>> getOrganizationGraphics(@RequestParam("isEnable") @ApiParam(value = "是否含有封存 0不含有、1含有",example = "0") Short isEnable,
                                                                            @RequestParam("orgId") @ApiParam(value = "机构ID",example = "0") Integer orgId){
        PageResult<Organization> pageResult = organizationService.getOrganizationGraphics(getUserSession(), isEnable, orgId);
        return new ResponseResult<>(pageResult);
    }


    @GetMapping("/getOrganizationList")
    @ApiOperation(value = "根据条件分页查询用户下所有的机构",notes = "高雄")
    public ResponseResult<PageResult<Organization>> getOrganizationList(OrganizationPageVo organizationPageVo){
        UserSession userSession = getUserSession();
        PageResult<Organization> pageResult = organizationService.getOrganizationList(organizationPageVo,userSession);
        return new ResponseResult<>(pageResult);
    }


    @PostMapping("/addOrganization")
    @ApiOperation(value = "新增机构",notes = "高雄")
    public ResponseResult addOrganization(@RequestBody OrganizationVo organizationVo){
        return organizationService.addOrganization(getUserSession(),organizationVo);
    }


    @PostMapping("/editOrganization")
    @ApiOperation(value = "编辑机构",notes = "高雄")
    public ResponseResult editOrganization(@RequestBody OrganizationVo organizationVo){
        //编辑机构旧的信息存入机构历史表
        return organizationService.editOrganization(organizationVo);
    }

    @GetMapping("/deleteOrganizationById")
    @ApiOperation(value = "删除机构",notes = "高雄")
    @ApiImplicitParam(name="orgIds", value = "机构id", paramType = "query", dataType = "int", allowMultiple = true, required = true)
    public ResponseResult deleteOrganizationById(List<Integer> orgIds){
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

    @ApiImplicitParams({
            @ApiImplicitParam(name = "preOrgId", value = "上个机构id", paramType = "query", dataType = "int",  required = true, example = "0103"),
            @ApiImplicitParam(name = "midOrgId", value = "需要排序机构的id", paramType = "query", dataType = "int", required = true, example = "0101"),
            @ApiImplicitParam(name = "nextOrgId", value = "下一个机构id", paramType = "query", dataType = "int", required = true, example = "0102"),
    })
    @GetMapping("/sortOrganization")
    @ApiOperation(value = "机构排序", notes = "高雄")
    public ResponseResult sortOrganization(@RequestParam("preOrgId")Integer preOrgId,
                                         @RequestParam("midOrgId")Integer midOrgId,
                                         @RequestParam("nextOrgId")Integer nextOrgId){
        return organizationService.sortOrganization(preOrgId, midOrgId, nextOrgId);
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
    public ResponseResult downloadTemplate(){
         //TODO
        return null;
    }


    @ApiOperation(value = "根据查询条件导出Excel", notes = "高雄")
    @PostMapping("/downloadExcelByCondition")
    public ResponseResult downloadExcelByCondition(@RequestBody OrganizationPageVo organizationPageVo){

        return null;
    }

    @ApiOperation(value = "根据选择的机构id导出Excel", notes = "高雄")
    @GetMapping("/downloadExcelByOrgId")
    public ResponseResult downloadExcelByOrgCode(@RequestParam("orgIds") @ApiParam(value = "所选机构的id",required = true) List<Integer> orgIds){

        return null;
    }

    @ApiOperation(value = "导入Excel", notes = "高雄")
    @PostMapping("/uploadExcel")
    public ResponseResult uploadExcel(@ApiParam(value = "需要导入的Excel文件", required = true) MultipartFile file){

        return null;
    }

    @ApiOperation(value = "机构职位树状图展示", notes = "高雄")
    @GetMapping("/getOrganizationPositionTree")
    public ResponseResult<List<Organization>> getOrganizationPositionTree(@ApiParam(value = "是否含有封存 0不含有、1含有",example = "0") Short isEnable){
        return organizationService.getOrganizationPositionTree(getUserSession(), isEnable);
    }
}
