package com.qinjee.masterdata.controller.organization;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.PositionGrade;
import com.qinjee.masterdata.model.vo.organization.PositionGradeVo;
import com.qinjee.masterdata.service.organation.PositionGradeService;
import com.qinjee.model.request.PageVo;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 彭洪思
 * @version 1.0.1
 * @Description TODO
 * @createTime 2020年03月01日 10:18:56
 */
@RequestMapping("/positionGrade")
@RestController
@Api(tags = "【机构管理】职等接口")
public class PositionGradeController extends BaseController {

  @Autowired
  private PositionGradeService positionGradeService;

  @GetMapping("/getPositionGradeList")
  @ApiOperation(value = "分页查询职等列表", notes = "彭洪思")
  public ResponseResult<PageResult<PositionGrade>> getPositionLevelListPage(PageVo pageVo) {
    return null;
  }

  @PostMapping("/addPositionGrade")
  @ApiOperation(value = "新增职等", notes = "彭洪思")
  public ResponseResult addPositionGrade(PositionGradeVo positionGradeVo) {
    return null;
  }

  @PostMapping("/editPositionGrade")
  @ApiOperation(value = "编辑职等", notes = "彭洪思")
  public ResponseResult editPositionGrade(PositionGradeVo positionGradeVo) {
    return null;
  }

  @GetMapping("/deletePositionGrade")
  @ApiOperation(value = "删除职等", notes = "彭洪思")
  @ApiImplicitParam(name = "positionGradeIds", value = "职等id", paramType = "query", dataType = "int", allowMultiple = true, required = true)
  public ResponseResult deletePositionGrade(List<Integer> positionGradeIds) {
    return positionGradeService.deletePositionGrade(getUserSession(), positionGradeIds);
  }

}
