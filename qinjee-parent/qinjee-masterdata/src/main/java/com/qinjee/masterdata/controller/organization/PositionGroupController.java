package com.qinjee.masterdata.controller.organization;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.PositionGroup;
import com.qinjee.masterdata.model.vo.organization.PositionGroupVo;
import com.qinjee.masterdata.service.organation.PositionGroupService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月11日 09:29:00
 */
@RestController
@RequestMapping("/positionGroup")
@Api(tags = "【机构管理】职位族接口")
public class PositionGroupController extends BaseController {

  @Autowired
  private PositionGroupService positionGroupService;

  @ApiOperation(value = "获取所有职位族", notes = "高雄")
  @GetMapping("/getAllPositionGroupTree")
  public ResponseResult<List<PositionGroup>> getAllPositionGroupTree() {
    return positionGroupService.getAllPositionGroupTree(getUserSession());
  }


  @ApiOperation(value = "新增职位族", notes = "高雄")
  @GetMapping("/addPositionGroup")
  public ResponseResult addPositionGroup(@RequestParam @ApiParam(value = "职位族名称", example = "研发族", required = true) String positionGroupName) {
    return positionGroupService.addPositionGroup(getUserSession(), positionGroupName);
  }

  @ApiOperation(value = "编辑职位族", notes = "高雄")
  @PostMapping("/editPositionGroup")
  public ResponseResult editPositionGroup(@RequestBody PositionGroupVo positionGroupVo) {
    return positionGroupService.editPositionGroup(getUserSession(), positionGroupVo);
  }

  @ApiOperation(value = "删除职位族", notes = "高雄")
  @PostMapping("/deletePositionGroup")
  public ResponseResult deletePositionGroup(@RequestBody List<Integer> positionGroupIds) {
    System.out.println(positionGroupIds);
    ResponseResult responseResult = new ResponseResult(CommonCode.SUCCESS);
    int i = positionGroupService.deletePositionGroup(positionGroupIds);
    responseResult.setMessage("删除成功（逻辑删除），删除了" + i + "条职位族");
    return responseResult;
  }

  @ApiImplicitParams({
      @ApiImplicitParam(name = "positionGroupIds", value = "需要排序的职位族id", paramType = "query", dataType = "int", allowMultiple = true, required = true)
  })
  @ApiOperation(value = "职位族排序", notes = "高雄")
  @GetMapping("/sortPositionGroup")
  public ResponseResult sortPositionGroup(@RequestParam LinkedList<String> positionGroupIds) {
    return positionGroupService.sortPositionGroup(positionGroupIds);
  }

  @ApiImplicitParam(name = "positionGroupIds", value = "选择的职位族id,不传默认导出所有d", paramType = "query", dataType = "int", allowMultiple = true)
  @ApiOperation(value = "导出excel", notes = "高雄")
  @GetMapping("/downloadExcel")
  public ResponseResult downloadExcel(@ApiParam(value = "导出路径", required = true) @RequestParam("filePath") String filePath, @RequestParam(value = "positionGroupIds", required = false) @ApiParam(value = "所选机构的id", required = false) List<Integer> positionGroupIds) {
    //TODO
    if (CollectionUtils.isEmpty(positionGroupIds)) {
      return positionGroupService.downloadAllPositionGroupToExcel(filePath, getUserSession());
    }
    return positionGroupService.downloadPositionGroupToExcelByOrgId(filePath, positionGroupIds, getUserSession());
  }
}
