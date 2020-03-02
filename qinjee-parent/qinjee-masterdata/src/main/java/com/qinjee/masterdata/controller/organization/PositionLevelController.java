package com.qinjee.masterdata.controller.organization;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.PositionLevel;
import com.qinjee.masterdata.model.vo.organization.PositionLevelVo;
import com.qinjee.masterdata.service.organation.PositionLevelService;
import com.qinjee.model.request.PageVo;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author 彭洪思
 * @version 1.0.1
 * @Description TODO
 * @createTime 2020年03月01日 10:13:00
 */
@RestController
@RequestMapping("/positionLevel")
@Api(tags = "【机构管理】职级接口")
public class PositionLevelController extends BaseController {

  @Autowired
  private PositionLevelService positionLevelService;


  @PostMapping("/list")
  @ApiOperation(value = "分页查询职级列表", notes = "彭洪思")
  public ResponseResult<PageResult<PositionLevelVo>> get(@RequestBody PageVo pageVo) {
    return null;
  }

  @PostMapping("/add")
  @ApiOperation(value = "新增职级", notes = "彭洪思")
  public ResponseResult addPositionLevel(PositionLevelVo positionLevelVo) {
    return positionLevelService.addPositionLevel(positionLevelVo, getUserSession());
  }

  @PostMapping("/editPositionLevel")
  @ApiOperation(value = "编辑职级", notes = "彭洪思")
  public ResponseResult editPositionLevel(PositionLevelVo positionLevelVo) {
    return positionLevelService.editPositionLevel(getUserSession(), positionLevelVo);
  }

  @PostMapping("/deletePositionLevel")
  @ApiOperation(value = "删除职级", notes = "彭洪思")
  @ApiImplicitParam(name = "positionLevelIds", value = "职级id", paramType = "query", dataType = "int", allowMultiple = true, required = true)
  public ResponseResult deletePositionLevel(List<Integer> positionLevelIds) {
    return positionLevelService.deletePositionLevel(positionLevelIds, getUserSession());
  }

}
