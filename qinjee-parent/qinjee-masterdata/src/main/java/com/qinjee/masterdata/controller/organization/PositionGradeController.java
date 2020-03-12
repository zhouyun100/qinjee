package com.qinjee.masterdata.controller.organization;

import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.PositionGrade;
import com.qinjee.masterdata.model.entity.PositionLevel;
import com.qinjee.masterdata.model.vo.organization.PositionGradeVo;
import com.qinjee.masterdata.service.organation.PositionGradeService;
import com.qinjee.model.request.PageVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/list")
    @ApiOperation(value = "分页查询职等列表", notes = "彭洪思")
    public ResponseResult<PageResult<PositionGrade>> list(PageVo pageVo) {
        if (!checkParam(getUserSession(), pageVo)) {
            return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
        }
        PageResult<PositionGrade> positionLevelList = positionGradeService.listPositionGrade(pageVo, getUserSession());
        return new ResponseResult<>(positionLevelList);
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增职等", notes = "彭洪思")
    public ResponseResult add(@RequestBody PositionGrade positionGrade) {
        if (!checkParam(getUserSession(), positionGrade)) {
            return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
        }
        int i = positionGradeService.addPositionGrade(positionGrade, getUserSession());
        if (i > 0) {
            return new ResponseResult();
        } else {
            return new ResponseResult(null, CommonCode.SERVER_ERROR);
        }

    }

    @PostMapping("/edit")
    @ApiOperation(value = "编辑职等", notes = "彭洪思")
    public ResponseResult edit(@RequestBody PositionGrade positionGrade) {
        if (!checkParam(getUserSession(), positionGrade)) {
            return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
        }
        int i = positionGradeService.editPositionGrade(positionGrade, getUserSession());
        if (i > 0) {
            return new ResponseResult();
        } else {
            return new ResponseResult(null, CommonCode.SERVER_ERROR);
        }

    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除职等", notes = "彭洪思")
    public ResponseResult delete(@RequestBody List<Integer> positionGradeIds) {
        if (!checkParam(getUserSession(), positionGradeIds)) {
            return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
        }
        int i = positionGradeService.batchDeletePositionGrade(getUserSession(), positionGradeIds);
        if (i > 0) {
            return new ResponseResult();
        } else {
            return new ResponseResult(null, CommonCode.SERVER_ERROR);
        }
    }

    @PostMapping("/sort")
    @ApiOperation(value = "职等排序", notes = "彭洪思")
    public ResponseResult sort(@RequestBody List<Integer> positionGradeIds) {
        if (!checkParam(getUserSession(), positionGradeIds)) {
            return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
        }
        positionGradeService.sortPositionGrade(getUserSession(), positionGradeIds);
        return new ResponseResult();
    }

}
