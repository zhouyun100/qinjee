package com.qinjee.masterdata.controller.organization;

import com.qinjee.exception.BusinessException;
import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.Position;
import com.qinjee.masterdata.model.entity.PositionGroup;
import com.qinjee.masterdata.model.vo.organization.PositionVo;
import com.qinjee.masterdata.model.vo.organization.page.PositionPageVo;
import com.qinjee.masterdata.service.organation.PositionGroupService;
import com.qinjee.masterdata.service.organation.PositionService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  @Autowired
  private PositionService positionService;@Autowired
  private PositionGroupService positionGroupService;

  private Boolean checkParam(Object... params) {
    for (Object param : params) {
      if (null == param || "".equals(param)) {
        return false;
      }
    }
    return true;
  }


  @ApiOperation(value = "ok，分页查询职位信息", notes = "高雄")
  @PostMapping("/getPositionPage")
  public ResponseResult<PageResult<Position>> getPositionPage(@RequestBody PositionPageVo positionPageVo) {
    return positionService.getPositionPage(getUserSession(), positionPageVo);
  }
  @ApiOperation(value = "ok，查询所有职位", notes = "高雄")
  @GetMapping("/getAllPositions")
  public ResponseResult<List<Position>> getAllPositions() {
    return positionService.getAllPositions(getUserSession());
  }

  @ApiOperation(value = "ok，新增职位", notes = "ok")
  @PostMapping("/addPosition")
  public ResponseResult addPosition(PositionVo positionVo) {
    return positionService.addPosition(positionVo, getUserSession());
  }

  @ApiOperation(value = "ok，编辑职位", notes = "ok")
  @PostMapping("/editPosition")
  public ResponseResult editPosition(PositionVo positionVo) {
    return positionService.editPosition(positionVo, getUserSession());
  }

  @ApiOperation(value = "ok，删除职位", notes = "ok")
  @PostMapping("/deletePosition")
  public ResponseResult deletePosition(@RequestBody List<Integer> positionIds) {
    return positionService.deletePosition(positionIds, getUserSession());
  }

  @ApiImplicitParams({
      @ApiImplicitParam(name = "prePositionId", value = "上个职位id", paramType = "query", dataType = "int", required = true, example = "3"),
      @ApiImplicitParam(name = "midPositionId", value = "需要排序的职位id", paramType = "query", dataType = "int", required = true, example = "1"),
      @ApiImplicitParam(name = "nextPositionId", value = "下一个职位id", paramType = "query", dataType = "int", required = true, example = "2"),
  })
  @ApiOperation(value = "未验证，职位排序", notes = "未验证")
  @GetMapping("/sortPositionGroup")
  public ResponseResult sortPosition(Integer prePositionId,
                                     Integer midPositionId,
                                     Integer nextPositionId) {
    return positionService.sortPosition(prePositionId, midPositionId, nextPositionId);
  }


  @ApiImplicitParam(name = "positionIds", value = "选择的职位id,不传默认导出所有d", paramType = "query", dataType = "int", allowMultiple = true)
  @ApiOperation(value = "未实现，导出职位excel", notes = "未实现")
  @GetMapping("/downloadExcel")
  public ResponseResult downloadExcelByOrg(@RequestParam("positionIds") List<Integer> positionIds) {

    return null;
  }

  /*  @ApiOperation(value = "新增岗位选择职位时带出职级职等", notes = "高雄")
    @GetMapping("/getPositionLevelAndGrade")
    public ResponseResult<Position> getPositionLevelAndGrade(@ApiParam(value = "职位id", example = "1", required = true) Integer positionId){
        return positionService.getPositionLevelAndGrade(positionId);
    }*/


  @PostMapping("/getPositionSystem")
  @ApiOperation(value = "ok,获取职位体系分页显示", notes = "ok")
  /**
   * 初始化(如果什么都不传)显示职位族树以及职位体系表所有内容
   */
  public ResponseResult getPositionSystem(PositionPageVo positionPageVo) throws Exception {
    Map<String,Object> resultMap=new HashMap<>();
    //如果都不传，返回所有
    if((positionPageVo.getPositionGroupId()==null||"".equals(positionPageVo.getPositionGroupId()))
        &&(positionPageVo.getPositionId()==null||"".equals(positionPageVo.getPositionId()))){
      //获取职位族
      ResponseResult<List<PositionGroup>> positionGroupTree = positionGroupService.getAllPositionGroupTree(getUserSession());
      //获取所有职位
      ResponseResult<PageResult<Position>> positionPage=positionService.getAllPositionPage(getUserSession());

      resultMap.put("positionGroupTree",positionGroupTree);
      resultMap.put("positionPage",positionPage);

    }
    //如果传了positionGroupId但没传positionId，则查询该positionGroupId下所有职位
    if((positionPageVo.getPositionGroupId()!=null&&!"".equals(positionPageVo.getPositionGroupId()))
        &&(positionPageVo.getPositionId()==null||"".equals(positionPageVo.getPositionId()))){
      ResponseResult<PageResult<Position>> positionPage = positionService.getPositionPage(getUserSession(), positionPageVo);
      resultMap.put("positionPage",positionPage);
    }
    if(positionPageVo.getPositionId()!=null&&!"".equals(positionPageVo.getPositionId())){
      PageResult<Position> positionPage=positionService.getPositionByPositionId(positionPageVo.getPositionId());
      resultMap.put("positionPage",positionPage);
    }

    //如果传了positionId，则只返回该职位
    return new ResponseResult<>(resultMap);
  }


}
