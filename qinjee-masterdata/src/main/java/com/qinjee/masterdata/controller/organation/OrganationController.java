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
@Api("机构相关接口")
public class OrganationController extends BaseController {


    @GetMapping("/getAllOrganation")
    @ApiOperation(value = "查询用户下所有的机构",notes = "高雄")
    public ResponseResult<PageResult<Organation>> getAllOrganation(@RequestParam("isEnable") @ApiParam(value = "是否含有封存",example = "0") Short isEnable){

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

        return null;
    }

    @GetMapping("/deleteOrganationById")
    @ApiOperation(value = "删除机构",notes = "高雄")
    public ResponseResult deleteOrganationById(@RequestParam("orgId") @ApiParam(value = "机构id",example = "1",required = true) Integer orgId){

        return null;
    }

    @GetMapping("/sealOrganationById")
    @ApiOperation(value = "封存/封存机构", notes = "高雄")
    public ResponseResult sealOrganationById(@RequestParam("orgCode") @ApiParam(value = "机构编码",example = "1",required = true) String orgCode,
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
            @ApiImplicitParam(name = "preOrgCode", value = "上个机构编码", paramType = "query", dataType = "String",  required = true, example = "0103"),
            @ApiImplicitParam(name = "midOrgCode", value = "需要排序机构的编码", paramType = "query", dataType = "String", required = true, example = "0101"),
            @ApiImplicitParam(name = "nextOrgCode", value = "下一个机构编码", paramType = "query", dataType = "String", required = true, example = "0102"),
    })
    @GetMapping("/sortOrganation")
    @ApiOperation(value = "机构排序", notes = "高雄")
    public ResponseResult sortOrganation(@RequestParam("preOrgCode")String preOrgCode,
                                         @RequestParam("midOrgCode")String midOrgCode,
                                         @RequestParam("nextOrgCode")String nextOrgCode){

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


    @ApiOperation(value = "导入Excel", notes = "高雄")
    @GetMapping("/downloadExcel")
    public ResponseResult downloadExcel(@ApiParam(value = "是否含有封存",example = "0") MultipartFile file){

        return null;
    }

    @ApiOperation(value = "导出", notes = "高雄")
    @GetMapping("/uploadExcelExcel")
    public ResponseResult uploadExcel(){

        return null;
    }

}
