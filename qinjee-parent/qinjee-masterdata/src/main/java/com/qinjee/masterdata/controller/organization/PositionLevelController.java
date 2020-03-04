package com.qinjee.masterdata.controller.organization;

import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.PositionLevel;
import com.qinjee.masterdata.service.organation.PositionLevelService;
import com.qinjee.model.request.PageVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

  private Boolean checkParam(Object... params) {
    for (Object param : params) {
      if (param instanceof UserSession) {
        if (null == param || "".equals(param)) {
          ExceptionCast.cast(CommonCode.INVALID_SESSION);
          return false;
        }
      }
      if (null == param || "".equals(param)) {
        return false;
      }
    }
    return true;
  }
  @PostMapping("/list")
  @ApiOperation(value = "分页查询职级列表", notes = "彭洪思")
  public ResponseResult<PageResult<PositionLevel>> get(@RequestBody PageVo pageVo) {
    if(!checkParam(getUserSession(),pageVo)){
      return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }
    PageResult<PositionLevel> positionLevelList = positionLevelService.listPositionLevel(pageVo, getUserSession());
    return new ResponseResult<>(positionLevelList);
  }

  @PostMapping("/add")
  @ApiOperation(value = "新增职级", notes = "彭洪思")
  public ResponseResult add(PositionLevel positionLevel) {
    if(!checkParam(getUserSession(),positionLevel)){
      return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }
    int i = positionLevelService.addPositionLevel(positionLevel, getUserSession());
    if (i>0){
      return new ResponseResult();
    }
    return new ResponseResult(CommonCode.SERVER_ERROR);
  }

  @PostMapping("/edit")
  @ApiOperation(value = "编辑职级", notes = "彭洪思")
  public ResponseResult edit(PositionLevel positionLevel) {
    if(!checkParam(getUserSession(),positionLevel)){
      return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }
    int i = positionLevelService.editPositionLevel(getUserSession(), positionLevel);
    if (i>0){
      return new ResponseResult();
    }
    return new ResponseResult(CommonCode.SERVER_ERROR);
  }

  @PostMapping("/delete")
  @ApiOperation(value = "删除职级", notes = "彭洪思")
  public ResponseResult delete(@RequestBody List<Integer> positionLevelIds) {
    if(!checkParam(getUserSession(),positionLevelIds)){
      return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }
    int i =positionLevelService.batchDeletePositionLevel(positionLevelIds,getUserSession());
    if (i>0){
      return new ResponseResult();
    }
    return new ResponseResult(CommonCode.SERVER_ERROR);
  }
  @PostMapping("/sort")
  @ApiOperation(value = "职级排序", notes = "彭洪思")
  public ResponseResult sort(List<Integer> positionLevelIds) {
    if(!checkParam(getUserSession(),positionLevelIds)){
      return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }
    return null;
  }

}
