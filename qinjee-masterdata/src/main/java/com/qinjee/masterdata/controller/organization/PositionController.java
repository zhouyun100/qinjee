package com.qinjee.masterdata.controller.organization;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.Position;
import com.qinjee.masterdata.model.vo.organization.PositionVo;
import com.qinjee.model.request.PageVo;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月11日 10:23:00
 */
@Api(tags = "【机构管理】职位接口")
@RestController
@RequestMapping("/position")
public class PositionController extends BaseController {

    @ApiOperation(value = "按职位展示职位体系")
    @GetMapping("/showByPosition")
    public ResponseResult<Position> showByPosition(){
        return null;
    }


    @ApiOperation(value = "分页查询职位信息", notes = "高雄")
    @GetMapping("/getPositionList")
    public ResponseResult<PageResult<Position>> getPositionList(PageVo pageVo){
        //返回值携带值等信息
        return null;
    }

    @ApiOperation(value = "新增职位", notes = "高雄")
    @PostMapping("/addPosition")
    public ResponseResult addPosition(){

        return null;
    }

    @ApiOperation(value = "编辑职位", notes = "高雄")
    @PostMapping("/editPosition")
    public ResponseResult editPosition(PositionVo positionVo){

        return null;
    }

    @ApiOperation(value = "删除职位", notes = "高雄")
    @GetMapping("/deletePosition")
    @ApiImplicitParam(name="positionIds", value = "职位id", paramType = "query", dataType = "int", allowMultiple = true, required = true)
    public ResponseResult editPosition(List<Integer> positionIds){

        return null;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "prePositionId", value = "上个职位id", paramType = "query", dataType = "int",  required = true, example = "3"),
            @ApiImplicitParam(name = "midPositionId", value = "需要排序的职位id", paramType = "query", dataType = "int", required = true, example = "1"),
            @ApiImplicitParam(name = "nextPositionId", value = "下一个职位id", paramType = "query", dataType = "int", required = true, example = "2"),
    })
    @ApiOperation(value = "职位排序", notes = "高雄")
    @GetMapping("/sortPositionGroup")
    public ResponseResult  sortPosition (Integer prePositionId,
                                         Integer midPositionId,
                                         Integer nextPositionId){

        return null;
    }

    @ApiImplicitParam(name = "positionIds", value = "选择的职位id,不传默认导出所有d", paramType = "query", dataType = "int", allowMultiple = true)
    @ApiOperation(value = "导出职位excel", notes = "高雄")
    @GetMapping("/downloadExcel")
    public ResponseResult downloadExcel(@RequestParam("positionIds") List<Integer> positionIds){

        return null;
    }























}
