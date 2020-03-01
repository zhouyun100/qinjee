package com.qinjee.masterdata.controller.organization;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.PositionGroup;
import com.qinjee.masterdata.model.vo.organization.PositionGroupVo;
import com.qinjee.masterdata.service.organation.PositionGroupService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/positionGroup")
@Api(tags = "【机构管理】职位族接口")
public class PositionGroupController extends BaseController {

  private static Logger logger = LogManager.getLogger(PositionGroupController.class);

  @Autowired
  private PositionGroupService positionGroupService;

  @ApiOperation(value = "展示职位族-职位树", notes = "彭洪思")
  @GetMapping("/getAllPositionGroupTree")
  public ResponseResult<List<PositionGroup>> getAllPositionGroupTree() {
    ResponseResult<List<PositionGroup>> allPositionGroupTree = positionGroupService.getAllPositionGroupTree(getUserSession());
    //logger.info("展示职位族-职位树：PositionGroupTree》"+allPositionGroupTree);
    return allPositionGroupTree;
  }


  @ApiOperation(value = "新增职位族", notes = "彭洪思")
  @GetMapping("/addPositionGroup")
  public ResponseResult addPositionGroup(@RequestParam @ApiParam(value = "职位族名称", example = "研发族", required = true) String positionGroupName) {
    //logger.info("新增职位族,positionGroupName》："+positionGroupName);
    return positionGroupService.addPositionGroup(getUserSession(), positionGroupName);
  }

  @ApiOperation(value = "编辑职位族", notes = "彭洪思")
  @PostMapping("/editPositionGroup")
  public ResponseResult editPositionGroup(@RequestBody PositionGroupVo positionGroupVo) {
    //logger.info("编辑职位族：positionGroupVo》"+positionGroupVo);
    return positionGroupService.editPositionGroup(getUserSession(), positionGroupVo);
  }

  @ApiOperation(value = "删除职位族", notes = "彭洪思")
  @PostMapping("/deletePositionGroup")
  public ResponseResult deletePositionGroup(@RequestBody List<Integer> positionGroupIds) {
    //logger.info("删除职位族:positionGroupIds》"+positionGroupIds);
    ResponseResult responseResult = new ResponseResult(CommonCode.SUCCESS);
    positionGroupService.deletePositionGroup(positionGroupIds);
    return responseResult;
  }

  @ApiImplicitParams({
      @ApiImplicitParam(name = "positionGroupIds", value = "需要排序的职位族id", paramType = "query", dataType = "int", allowMultiple = true, required = true)
  })
  @ApiOperation(value = "职位族排序", notes = "彭洪思")
  @GetMapping("/sortPositionGroup")
  public ResponseResult sortPositionGroup(@RequestParam LinkedList<String> positionGroupIds) {
    //logger.info("职位族排序:positionGroupIds》"+positionGroupIds);
    return positionGroupService.sortPositionGroup(positionGroupIds);
  }
}
