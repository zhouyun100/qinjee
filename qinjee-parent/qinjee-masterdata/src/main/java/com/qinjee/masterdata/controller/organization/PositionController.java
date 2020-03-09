package com.qinjee.masterdata.controller.organization;

import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.Position;
import com.qinjee.masterdata.model.vo.organization.PositionVo;
import com.qinjee.masterdata.model.vo.organization.bo.PositionPageBO;
import com.qinjee.masterdata.service.organation.PositionService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author 彭洪思
 * @version 1.0.1
 * @Description
 * @createTime 2020年02月28日 10:23:00
 */
@Api(tags = "【机构管理】职位接口")
@RestController
@RequestMapping("/position")
public class PositionController extends BaseController {
    private static Logger logger = LogManager.getLogger(PositionController.class);

    @Autowired
    private PositionService positionService;

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


    @ApiOperation(value = "ok，分页查询职位信息", notes = "彭洪思")
    @PostMapping("/getPositionPage")
    public ResponseResult<PageResult<Position>> getPositionPage(@RequestBody PositionPageBO positionPageBO) {
        if (checkParam(positionPageBO, getUserSession())) {
            long start = System.currentTimeMillis();
            ResponseResult<PageResult<Position>> positionPage = positionService.getPositionPage(positionPageBO);
            logger.info("分页查询职位信息耗时:" + (System.currentTimeMillis() - start));
            return positionPage;
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);

    }

    @ApiOperation(value = "ok，新增职位", notes = "ok")
    @PostMapping("/addPosition")
    public ResponseResult addPosition(PositionVo positionVo) {
        if (checkParam(positionVo, getUserSession())) {
            long start = System.currentTimeMillis();
            ResponseResult responseResult = positionService.addPosition(positionVo, getUserSession());
            logger.info("新增职位耗时:" + (System.currentTimeMillis() - start));
            return responseResult;
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);

    }

    @ApiOperation(value = "ok，保证职位名称在同一家企业下唯一", notes = "ok")
    @PostMapping("/determinePositionNameIsOnly")
    public ResponseResult determinePositionNameIsOnly(String positionName) {
        Boolean b = checkParam(positionName, getUserSession());
        if (b) {
            long start = System.currentTimeMillis();
            positionService.determinePositionNameIsOnly(positionName, getUserSession());
            logger.info("校验职位名称在同一家企业下唯一耗时:" + (System.currentTimeMillis() - start));
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }


    @ApiOperation(value = "ok，编辑职位", notes = "ok")
    @PostMapping("/editPosition")
    public ResponseResult editPosition(PositionVo positionVo) {
        Boolean b = checkParam(positionVo, getUserSession());
        if (b) {
            long start = System.currentTimeMillis();
            positionService.editPosition(positionVo, getUserSession());
            logger.info("编辑职位耗时:" + (System.currentTimeMillis() - start));
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }

    @ApiOperation(value = "ok，删除职位", notes = "ok")
    @PostMapping("/deletePosition")
    public ResponseResult deletePosition(@RequestBody List<Integer> positionIds) {
        Boolean b = checkParam(positionIds, getUserSession());
        if (b) {
            long start = System.currentTimeMillis();
            ResponseResult responseResult = positionService.deletePosition(positionIds, getUserSession());
            logger.info("删除职位耗时:" + (System.currentTimeMillis() - start));
            return responseResult;
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);

    }


    @PostMapping("/sortPosition")
    @ApiOperation(value = "ok，职位排序，只能同一级别下机构排序（需要将该级下所有职位的id按顺序传参）", notes = "ok")
    public ResponseResult sortOrganizationInOrg(@RequestBody LinkedList<Integer> positionIds) {
        //参数校验
        if (checkParam(positionIds)) {
            long start = System.currentTimeMillis();
            positionService.sortPosition(positionIds);
            logger.info("职位排序耗时:" + (System.currentTimeMillis() - start));
            return ResponseResult.SUCCESS();
        }
        return new ResponseResult<>(null, CommonCode.INVALID_PARAM);
    }
}
