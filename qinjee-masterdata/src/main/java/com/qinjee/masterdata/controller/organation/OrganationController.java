package com.qinjee.masterdata.controller.organation;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.Organation;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

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
    public ResponseResult<PageResult<Organation>> getAllOrganation(){

        return null;
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "每页大小", paramType = "query", dataType = "Integer", required = true, example = "10"),
            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", dataType = "Integer", required = true, example = "1")
    })
    @GetMapping("/getOrganationList")
    @ApiOperation(value = "分页查询用户下所有的机构",notes = "高雄")
    public ResponseResult<PageResult<Organation>> getOrganationList(@RequestParam("pageSize") Integer pageSize, @RequestParam("currentPage") Integer currentPage){

        return null;
    }


    @PostMapping("/addOrganation")
    @ApiOperation(value = "新增机构",notes = "高雄")
    public ResponseResult addOrganation(@RequestBody Organation organation){

        return null;
    }

}
