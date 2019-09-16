package com.qinjee.masterdata.controller.organization;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.Organization;
import com.qinjee.masterdata.model.entity.UserArchive;
import com.qinjee.masterdata.model.vo.organization.OrganizationPageVo;
import com.qinjee.masterdata.model.vo.organization.OrganizationVo;
import com.qinjee.masterdata.service.organation.OrganizationService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
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
        return new ResponseResult<>(pageResult, CommonCode.SUCCESS);
    }


    @GetMapping("/getOrganizationList")
    @ApiOperation(value = "根据条件分页查询用户下所有的机构",notes = "高雄")
    public ResponseResult<PageResult<Organization>> getOrganizationList(OrganizationPageVo organizationPageVo){
        PageResult<Organization> pageResult = organizationService.getOrganizationList(organizationPageVo);
        return new ResponseResult<>(pageResult, CommonCode.SUCCESS);
    }


    @PostMapping("/addOrganization")
    @ApiOperation(value = "新增机构",notes = "高雄")
    public ResponseResult addOrganization(@RequestBody OrganizationVo organizationVo){

        return null;
    }


    @PostMapping("/editOrganization")
    @ApiOperation(value = "编辑机构",notes = "高雄")
    public ResponseResult editOrganization(@RequestBody OrganizationVo organizationVo){
        //编辑机构旧的信息存入机构历史表
        return null;
    }

    @GetMapping("/deleteOrganizationById")
    @ApiOperation(value = "删除机构",notes = "高雄")
    @ApiImplicitParam(name="orgIds", value = "机构id", paramType = "query", dataType = "int", allowMultiple = true, required = true)
    public ResponseResult deleteOrganizationById(List<Integer> orgIds){

        return null;
    }

    @GetMapping("/sealOrganizationByIds")
    @ApiOperation(value = "封存/封存机构", notes = "高雄")
    public ResponseResult sealOrganizationByIds(@RequestParam("orgCode") @ApiParam(value = "机构编码",example = "1",allowMultiple = true, required = true) List<String> orgCodes,
                                             @RequestParam("isEnable") @ApiParam(value = "0 封存、1 解封",example = "0") Short isEnable){

        return null;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "newOrgName", value = "新机构名称", paramType = "query", dataType = "String", required = true, example = "新机构名称"),
            @ApiImplicitParam(name = "newOrgCode", value = "新机构编码", paramType = "query", dataType = "String", required = true, example = "0101"),
            @ApiImplicitParam(name = "targetOrgCode", value = "归属机构", paramType = "query", dataType = "String" , required = true, example = "0103"),
            @ApiImplicitParam(name = "orgCodes", value = "待合并机构", paramType = "query", dataType = "String", allowMultiple = true, required = true),
    })
    @GetMapping("/mergeOrganization")
    @ApiOperation(value = "合并机构", notes = "高雄")
    public ResponseResult mergeOrganization(@RequestParam("newOrgName") String newOrgName,
                                          @RequestParam("newOrgCode") String newOrgCode,
                                          @RequestParam("targetOrgCode") String targetOrgCode,
                                          @RequestParam("orgCodes") List<String> orgCodes){
        return null;
    }

    @GetMapping("/getUserArchiveListByUserName")
    @ApiOperation(value = "机构负责人查询")
    public ResponseResult<UserArchive> getUserArchiveListByUserName(@ApiParam(value = "姓名", example = "张三", required = true) String userName){

        return null;
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

        return null;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "orgCodes", value = "需要合并机构的编码", paramType = "query", dataType = "String", allowMultiple = true, required = true, example = "0102"),
            @ApiImplicitParam(name = "targetOrgCode", value = "目标机构", paramType = "query", dataType = "String", required = true, example = "0101"),
    })
    @GetMapping("/transferOrganization")
    @ApiOperation(value = "划转机构", notes = "高雄")
    public ResponseResult transferOrganization(@RequestParam("orgCodes") List<String> orgCodes,
                                             @RequestParam("targetOrgCode") String targetOrgCode){

        return null;
    }

    @ApiOperation(value = "下载模板", notes = "高雄")
    @GetMapping("/downloadTemplate")
    public ResponseResult downloadTemplate(){

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


    @ApiOperation(value = "根据机构id查询本级及以下的机构")
    @GetMapping("/getOrganizationListByOrgId")
    public ResponseResult<List<Organization>> getOrganizationListByOrgId(@RequestParam("orgId") @ApiParam(value = "选择的机构id", example = "0101", required = true) String orgId){

        return null;
    }

}
