package com.qinjee.masterdata.controller.organization;

import com.qinjee.exception.BusinessException;
import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.dao.organation.PostDao;
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
import java.util.LinkedList;
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
    private PositionService positionService;
    @Autowired
    private PostDao postDao;
    @Autowired
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
        return positionService.getPositionPage( positionPageVo);
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

    @ApiOperation(value = "ok，保证职位名称在同一家企业下唯一", notes = "ok")
    @PostMapping("/determinePositionNameIsOnly")
    public ResponseResult determinePositionNameIsOnly(String positionName) {
        Boolean b = checkParam(positionName);
        if (b) {
            positionService.determinePositionNameIsOnly(positionName, getUserSession());
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
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


    @PostMapping("/sortPosition")
    @ApiOperation(value = "ok，职位排序，只能同一级别下机构排序（需要将该级下所有职位的id按顺序传参）", notes = "ok")
    public ResponseResult sortOrganizationInOrg(@RequestBody LinkedList<Integer> positionIds) {
        //参数校验
        if (checkParam(positionIds)) {
            positionService.sortPosition(positionIds);
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
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


}
