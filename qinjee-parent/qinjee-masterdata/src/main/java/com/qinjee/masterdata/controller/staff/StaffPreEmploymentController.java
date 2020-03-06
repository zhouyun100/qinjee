package com.qinjee.masterdata.controller.staff;

import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.PreEmployment;
import com.qinjee.masterdata.model.vo.staff.DetailCount;
import com.qinjee.masterdata.model.vo.staff.FieldValueForSearch;
import com.qinjee.masterdata.model.vo.staff.PreEmploymentVo;
import com.qinjee.masterdata.model.vo.staff.StatusChangeVo;
import com.qinjee.masterdata.model.vo.staff.archiveInfo.PreRegistVo;
import com.qinjee.masterdata.service.staff.IStaffPreEmploymentService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/staffpre")
@Api(tags = "【人员管理】预入职相关接口")
public class StaffPreEmploymentController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(StaffPreEmploymentController.class);
    private static final String CHANGSTATUS_READY = "已入职";
    @Autowired
    private IStaffPreEmploymentService staffPreEmploymentService;


    /**
     * 员工登记表打印的数据查询接口
     */
    @PostMapping(value = "/getEmploymentRegisterInfo")
    @ApiOperation(value = "员工登记表打印的数据查询接口", notes = "phs")
    public ResponseResult<List<PreRegistVo>> getEmploymentRegisterInfo(@RequestBody List<Integer> employmentIds ) throws IllegalAccessException {
        Boolean b = checkParam(employmentIds,getUserSession());
        if(b){
           List<PreRegistVo> preRegistList= staffPreEmploymentService.getEmploymentRegisterInfo(employmentIds);
           return new ResponseResult(preRegistList);
        }
        return  failResponseResult("参数错误");
    }




    /**
     * 新增预入职
     * 预入职表与档案表用物理表进行对应，此时需要物理表的存在，新增两个自定义表，新增n个自定义字段，物理字段名与物理属性名分别是物理表与属性的对应
     * 在项目初始化应该建好此表
     */
    @RequestMapping(value = "/insertPreEmployment", method = RequestMethod.POST)
    @ApiOperation(value = "新增预入职", notes = "hkt")
//    @ApiImplicitParam(name = "PreEmployment", value = "PreEmployment", paramType = "form", required = true)
    public ResponseResult insertPreEmployment(@RequestBody @Valid PreEmploymentVo preEmploymentVo ) throws Exception {
        Boolean b = checkParam(preEmploymentVo,getUserSession());
        if(b){
                staffPreEmploymentService.insertPreEmployment(preEmploymentVo,getUserSession());
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 删除预入职
     */
    @RequestMapping(value = "/deletePreEmployment", method = RequestMethod.POST)
    @ApiOperation(value = "删除预入职", notes = "hkt")
//    @ApiImplicitParam(name = "list", value = "预入职id集合", paramType = "query", required = true)
    public ResponseResult deletePreEmployment(@RequestBody List<Integer> list ) throws Exception {
        Boolean b = checkParam(list);
        if(b){
                staffPreEmploymentService.deletePreEmployment(list);
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("参数错误");
    }


    /**
     * 修改预入职信息(值的信息)
     */
    @CrossOrigin
    @RequestMapping(value = "/updatePreEmployment", method = RequestMethod.POST)
    @ApiOperation(value = "修改预入职信息(值的信息)", notes = "hkt")
//    @ApiImplicitParam(name = "PreEmployment", value = "PreEmployment", paramType = "form",  required = true)
    public ResponseResult updatePreEmployment(@RequestBody @Valid PreEmploymentVo preEmploymentVo,HttpServletResponse response){
        Boolean b = checkParam(preEmploymentVo,response);
        response.setHeader ( "Access-Control-Allow-Origin","*" );
        if(b){
                staffPreEmploymentService.updatePreEmployment(preEmploymentVo);
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("参数错误");
    }


    /**
     * 修改预入职信息(显示字段的信息)
     */
    @CrossOrigin
    @RequestMapping(value = "/updatePreEmploymentField", method = RequestMethod.POST)
    @ApiOperation(value = "修改预入职信息(显示字段的信息)", notes = "hkt")
//    @ApiImplicitParam(name = "map", value = "字段id与对应的字段名", paramType = "form",  required = true)
    public ResponseResult updatePreEmploymentField(@RequestBody Map<Integer,String> map) throws Exception {
        Boolean b = checkParam(map);
        if(b){
                staffPreEmploymentService.updatePreEmploymentField(map);
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 查看预入职信息(显示字段的信息)
     */
    @RequestMapping(value = "/selectPreEmploymentField", method = RequestMethod.POST)
    @ApiOperation(value = "查看预入职信息(显示字段的信息)，返回值中map是物理表属性，value是字段名", notes = "hkt")
    public ResponseResult<Map<String,String>> selectPreEmploymentField(){
        Boolean b = checkParam(getUserSession());
        if(b){
                Map<String, String> stringStringMap = staffPreEmploymentService.selectPreEmploymentField(getUserSession());
                return new ResponseResult<>(stringStringMap,CommonCode.SUCCESS);
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }


    /**
     * 查看单个预入职信息
     */
    @RequestMapping(value = "/selectPreEmploymentSingle", method = RequestMethod.POST)
    @ApiOperation(value = "查看单个预入职信息", notes = "hkt")
    public ResponseResult< PreEmployment > selectPreEmploymentSingle(Integer employeeId){
        Boolean b = checkParam(employeeId);
        if(b){
                PreEmployment preEmployment=staffPreEmploymentService.selectPreEmploymentSingle((employeeId));
                return new ResponseResult<>(preEmployment,CommonCode.SUCCESS);
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }


    /**
     * 根据机构查看预入职
     */
    @RequestMapping(value = "/selectPreEmployment", method = RequestMethod.POST)
    @ApiOperation(value = "根据机构查看预入职(物理表)", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "机构id", paramType = "query", required = true),
//            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
//            @ApiImplicitParam(name = "PageSize", value = "页大小", paramType = "query", required = true),
//
//    })
    public ResponseResult<PageResult<PreEmploymentVo>> selectPreEmployment(
                                                                         Integer currentPage,
                                                                         Integer pageSize){
        Boolean b = checkParam(getUserSession (),currentPage,pageSize);
        if(b){

                PageResult<PreEmploymentVo> pageResult =
                        staffPreEmploymentService.selectPreEmployment(getUserSession (),currentPage, pageSize);
                    return new ResponseResult<>(pageResult,CommonCode.SUCCESS);
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }


    /**
     * 延期入职以及放弃入职(延期入职时间还需要添加时间)
     * 延期入职需要更新预入职信息的入职时间，同时更新预入职更改表
     * 确认入职需要更新预入职表中的入职状态，需要更新预入职更改表，需要将数据同步到档案表
     * 鉴于更改表与预入职表中字段有出入，无法完全获取其中的字段值，故需要传更改表的数据过来
     */

    @RequestMapping(value = "/updatePreEmploymentChange", method = RequestMethod.POST)
    @ApiOperation(value = "延期入职,放弃入职以及黑名单", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "PreEmploymentId", value = "预入职表id", paramType = "query", required = true),
//            @ApiImplicitParam(name = "StatusChangeVo", value = "预入职变更表vo类", paramType = "form", required = true),
//    })
    public ResponseResult updatePreEmploymentChange(@RequestBody @Valid StatusChangeVo statusChangeVo) throws Exception {
        Boolean b = checkParam(statusChangeVo,getUserSession());
        if(b){
                staffPreEmploymentService.insertStatusChange(getUserSession(), statusChangeVo);
                return ResponseResult.SUCCESS();
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 确认入职
     */
    @RequestMapping(value = "/confirmPreemployment", method = RequestMethod.POST)
    @ApiOperation(value = "确认入职", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "PreEmploymentId", value = "预入职表id", paramType = "query", required = true),
//            @ApiImplicitParam(name = "StatusChangeVo", value = "预入职变更表vo类", paramType = "form", required = true),
//    })
    public ResponseResult confirmPreemployment(@RequestBody List<Integer> list) throws Exception {
        Boolean b = checkParam ( list, getUserSession () );
        if (b) {
                staffPreEmploymentService.confirmEmployment ( list,getUserSession ());
                return ResponseResult.SUCCESS ();
        }
        return failResponseResult ( "参数错误" );
    }
    /**
     * 待办提醒任务
     */
    @RequestMapping(value = "/getReadyCount", method = RequestMethod.GET)
    @ApiOperation(value = "待办提醒任务", notes = "hkt")
    //    @ApiImplicitParams({
//            @ApiImplicitParam(name = "PreEmploymentId", value = "预入职表id", paramType = "query", required = true),
//            @ApiImplicitParam(name = "StatusChangeVo", value = "预入职变更表vo类", paramType = "form", required = true),
//    })
    public ResponseResult<DetailCount> getReadyCount() {
        Boolean b = checkParam ( getUserSession () );
        if (b) {
            DetailCount detailCount=staffPreEmploymentService.getReadyCount ( getUserSession ());
            return new ResponseResult<>(detailCount,CommonCode.SUCCESS);
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }
    /**
     * 预入职表头查询
     */
    @RequestMapping(value = "/searchByHead", method = RequestMethod.POST)
    @ApiOperation(value = "预入职表头查询", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "机构id", paramType = "query", required = true),
//            @ApiImplicitParam(name = "currentPage", value = "当前页", paramType = "query", required = true),
//            @ApiImplicitParam(name = "PageSize", value = "页大小", paramType = "query", required = true),
//
//    })
    public ResponseResult<PageResult<PreEmploymentVo>> searchByHead(Integer currentPage, Integer pageSize, List<FieldValueForSearch> list){
        Boolean b = checkParam(getUserSession (),currentPage,pageSize,list);
        if(b){
            PageResult<PreEmploymentVo> pageResult =
                    staffPreEmploymentService.searchByHead(getUserSession (),currentPage, pageSize,list);
            return new ResponseResult<>(pageResult,CommonCode.SUCCESS);
        }
        return new ResponseResult<>(null,CommonCode.INVALID_PARAM);
    }

    private Boolean checkParam(Object... params) {
        for (Object param : params) {
            if(param instanceof UserSession){
                if(null == param|| "".equals(param)){
                    ExceptionCast.cast ( CommonCode.INVALID_SESSION );
                    return false;
                }
            }
            if (null == param || "".equals(param)) {
                return false;
            }
        }
        return true;
    }
    private ResponseResult failResponseResult(String message){
        ResponseResult fail = ResponseResult.FAIL();
        fail.setMessage(message);
        logger.error(message);
        return fail;
    }

}
