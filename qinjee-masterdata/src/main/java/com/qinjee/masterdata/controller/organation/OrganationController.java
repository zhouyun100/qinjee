package com.qinjee.masterdata.controller.organation;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.Organation;
import com.qinjee.masterdata.model.vo.OrganationPageVo;
import com.qinjee.masterdata.model.vo.OrganationVo;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description 机构controller
 * @createTime 2019年09月10日 10:19:00
 */
@RequestMapping("/organation")
@RestController
@Api(tags = "机构相关接口")
public class OrganationController extends BaseController {


    @GetMapping("/getOrganationTree")
    @ApiOperation(value = "根据是否封存查询用户下所有的机构,树形结构展示",notes = "高雄")
    public ResponseResult<PageResult<Organation>> getOrganationTree(@RequestParam("isEnable") @ApiParam(value = "是否含有封存 0不含有、1含有",example = "0") Short isEnable){

        return null;
    }


    @PostMapping("/getOrganationList")
    @ApiOperation(value = "根据条件分页查询用户下所有的机构",notes = "高雄")
    public ResponseResult<PageResult<Organation>> getOrganationList(@RequestBody OrganationPageVo organationPageVo){

        return null;
    }


    @PostMapping("/addOrganation")
    @ApiOperation(value = "新增机构",notes = "高雄")
    public ResponseResult addOrganation(@RequestBody OrganationVo organationVo){

        return null;
    }


    @PostMapping("/editOrganation")
    @ApiOperation(value = "编辑机构",notes = "高雄")
    public ResponseResult editOrganation(@RequestBody OrganationVo organationVo){
        //编辑机构旧的信息存入机构历史表
        return null;
    }

    @GetMapping("/deleteOrganationById")
    @ApiOperation(value = "删除机构",notes = "高雄")
    @ApiImplicitParam(name="orgIds", value = "机构id", paramType = "query", dataType = "int", allowMultiple = true, required = true)
    public ResponseResult deleteOrganationById(List<Integer> orgIds){

        return null;
    }

    @GetMapping("/sealOrganationByIds")
    @ApiOperation(value = "封存/封存机构", notes = "高雄")
    public ResponseResult sealOrganationByIds(@RequestParam("orgCode") @ApiParam(value = "机构编码",example = "1",allowMultiple = true, required = true) List<String> orgCodes,
                                             @RequestParam("isEnable") @ApiParam(value = "0 封存、1 解封",example = "0") Short isEnable){

        return null;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "newOrgName", value = "新机构名称", paramType = "query", dataType = "String", required = true, example = "新机构名称"),
            @ApiImplicitParam(name = "newOrgCode", value = "新机构编码", paramType = "query", dataType = "String", required = true, example = "0101"),
            @ApiImplicitParam(name = "targetOrgCode", value = "归属机构", paramType = "query", dataType = "String" , required = true, example = "0103"),
            @ApiImplicitParam(name = "orgCodes", value = "待合并机构", paramType = "query", dataType = "String", allowMultiple = true, required = true),
    })
    @GetMapping("/mergeOrganation")
    @ApiOperation(value = "合并机构", notes = "高雄")
    public ResponseResult mergeOrganation(@RequestParam("newOrgName") String newOrgName,
                                          @RequestParam("newOrgCode") String newOrgCode,
                                          @RequestParam("targetOrgCode") String targetOrgCode,
                                          @RequestParam("orgCodes") List<String> orgCodes){
        return null;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "preOrgId", value = "上个机构id", paramType = "query", dataType = "int",  required = true, example = "0103"),
            @ApiImplicitParam(name = "midOrgId", value = "需要排序机构的id", paramType = "query", dataType = "int", required = true, example = "0101"),
            @ApiImplicitParam(name = "nextOrgId", value = "下一个机构id", paramType = "query", dataType = "int", required = true, example = "0102"),
    })
    @GetMapping("/sortOrganation")
    @ApiOperation(value = "机构排序", notes = "高雄")
    public ResponseResult sortOrganation(@RequestParam("preOrgId")Integer preOrgId,
                                         @RequestParam("midOrgId")Integer midOrgId,
                                         @RequestParam("nextOrgId")Integer nextOrgId){

        return null;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "orgCodes", value = "需要合并机构的编码", paramType = "query", dataType = "String", allowMultiple = true, required = true, example = "0102"),
            @ApiImplicitParam(name = "targetOrgCode", value = "目标机构", paramType = "query", dataType = "String", required = true, example = "0101"),
    })
    @GetMapping("/transferOrganation")
    @ApiOperation(value = "划转机构", notes = "高雄")
    public ResponseResult transferOrganation(@RequestParam("orgCodes") List<String> orgCodes,
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
    public ResponseResult downloadExcelByCondition(@RequestBody OrganationPageVo organationPageVo){

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
    @GetMapping("/getOrganationListByOrgId")
    public ResponseResult<List<Organation>> getOrganationListByOrgId(@RequestParam("orgId") @ApiParam(value = "选择的机构id", example = "0101", required = true) String orgId){

        return null;
    }

}
