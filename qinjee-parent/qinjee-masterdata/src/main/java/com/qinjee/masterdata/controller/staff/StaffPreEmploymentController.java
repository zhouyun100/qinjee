package com.qinjee.masterdata.controller.staff;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.vo.staff.ConfirmId;
import com.qinjee.masterdata.model.vo.staff.EmailSendVo;
import com.qinjee.masterdata.model.vo.staff.PreEmploymentVo;
import com.qinjee.masterdata.model.vo.staff.StatusChangeVo;
import com.qinjee.masterdata.service.staff.IStaffPreEmploymentService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
     * 新增预入职
     * 预入职表与档案表用物理表进行对应，此时需要物理表的存在，新增两个自定义表，新增n个自定义字段，物理字段名与物理属性名分别是物理表与属性的对应
     * 在项目初始化应该建好此表
     */
    @RequestMapping(value = "/insertPreEmployment", method = RequestMethod.POST)
    @ApiOperation(value = "新增预入职", notes = "hkt")
//    @ApiImplicitParam(name = "PreEmployment", value = "PreEmployment", paramType = "form", required = true)
    public ResponseResult insertPreEmployment(@RequestBody @Valid PreEmploymentVo preEmploymentVo ){
        Boolean b = checkParam(preEmploymentVo,getUserSession());
        if(b){
            try {
                staffPreEmploymentService.insertPreEmployment(preEmploymentVo,getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("新增预入职失败");
            }
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 删除预入职
     */
    @RequestMapping(value = "/deletePreEmployment", method = RequestMethod.POST)
    @ApiOperation(value = "删除预入职", notes = "hkt")
//    @ApiImplicitParam(name = "list", value = "预入职id集合", paramType = "query", required = true)
    public ResponseResult deletePreEmployment(@RequestBody List<Integer> list ){
        Boolean b = checkParam(list);
        if(b){
            try {
                staffPreEmploymentService.deletePreEmployment(list);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("删除预入职失败");
            }
        }
        return  failResponseResult("参数错误");
    }
    /**
     * 修改预入职信息(值的信息)
     */
    @RequestMapping(value = "/updatePreEmployment", method = RequestMethod.POST)
    @ApiOperation(value = "修改预入职信息(值的信息)", notes = "hkt")
//    @ApiImplicitParam(name = "PreEmployment", value = "PreEmployment", paramType = "form",  required = true)
    public ResponseResult updatePreEmployment(@RequestBody @Valid PreEmploymentVo preEmploymentVo){
        Boolean b = checkParam(preEmploymentVo,getUserSession());
        if(b){
            try {
                staffPreEmploymentService.updatePreEmployment(preEmploymentVo,getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("修改预入职信息失败");
            }
        }
        return  failResponseResult("参数错误");
    }
    /**
     * 修改预入职信息(显示字段的信息)
     */
    @RequestMapping(value = "/updatePreEmploymentField", method = RequestMethod.POST)
    @ApiOperation(value = "修改预入职信息(显示字段的信息)", notes = "hkt")
//    @ApiImplicitParam(name = "map", value = "字段id与对应的字段名", paramType = "form",  required = true)
    public ResponseResult updatePreEmploymentField(@RequestBody Map<Integer,String> map){
        Boolean b = checkParam(map);
        if(b){
            try {
                staffPreEmploymentService.updatePreEmploymentField(map);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("修改预入职信息失败");
            }
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
            try {
                Map<String, String> stringStringMap = staffPreEmploymentService.selectPreEmploymentField(getUserSession());
                return new ResponseResult<>(stringStringMap,CommonCode.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
               return new ResponseResult<>(null,CommonCode.BUSINESS_EXCEPTION);
            }
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
            try {
                PageResult<PreEmploymentVo> pageResult =
                        staffPreEmploymentService.selectPreEmployment(getUserSession (),currentPage, pageSize);
                if(pageResult.getList().size()>0){
                    return new ResponseResult<>(pageResult,CommonCode.SUCCESS);
                }
                return new ResponseResult<>(null,CommonCode.FAIL_VALUE_NULL);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseResult<>(null,CommonCode.BUSINESS_EXCEPTION);
            }
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
    public ResponseResult updatePreEmploymentChange(@RequestBody @Valid StatusChangeVo statusChangeVo) {
        Boolean b = checkParam(statusChangeVo,getUserSession());
        if(b){
            try {
                staffPreEmploymentService.insertStatusChange(getUserSession(), statusChangeVo);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("延期入职以及放弃入职失败");
            }
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
    public ResponseResult confirmPreemployment(@RequestBody ConfirmId confirmId) {
        Boolean b = checkParam(confirmId,getUserSession());
        if(b){
            try {
                StatusChangeVo statusChangeVo=new StatusChangeVo();
                statusChangeVo.setAbandonReason("");
                statusChangeVo.setChangeState(CHANGSTATUS_READY);
                statusChangeVo.setPreEmploymentList(confirmId.getList () );
                statusChangeVo.setRuleId ( confirmId.getRuleId () );
                staffPreEmploymentService.insertStatusChange(getUserSession(), statusChangeVo);
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                e.printStackTrace();
                return failResponseResult("确认入职失败");
            }
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 发邮件给预入职人员
     */
    @RequestMapping(value = "/sendMail", method = RequestMethod.POST)
    @ApiOperation(value = "邮箱发送入职登记表", notes = "hkt")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "list", value = "预入职收信人id集合", paramType = "query", required = true),
//            @ApiImplicitParam(name = "list", value = "抄送人档案id集合", paramType = "query", required = true),
//            @ApiImplicitParam(name = "String", value = "发送邮件主题", paramType = "query", required = true),
//            @ApiImplicitParam(name = "String", value = "发送邮件内容", paramType = "query", required = true),
//            @ApiImplicitParam(name = "String[]", value = "发送邮件附件路径的数组", paramType = "query", required = true),
//    })
    public ResponseResult sendMail(@RequestBody EmailSendVo emailSendVo) {
        Boolean b = checkParam(emailSendVo,getUserSession ());
        if(b){
            try {
                staffPreEmploymentService.sendManyMail(emailSendVo,getUserSession ()) ;
                return ResponseResult.SUCCESS ();
            } catch (Exception e) {
                return failResponseResult("发邮件给预入职人员失败");
            }
        }
        return  failResponseResult("参数错误");
    }

    /**
     * 生成入职登记二维码,返回是二维码图片链接
     */
    @RequestMapping(value = "/creatQrcodeForPre", method = RequestMethod.POST)
    @ApiOperation(value = "生成入职登记二维码", notes = "hkt")
    @ApiImplicitParam(name = "url", value = "链接地址", paramType = "query", required = true)
    public ResponseResult creatQrcodeForPre(String url) {

        return  new ResponseResult<>("qrcode path", CommonCode.SUCCESS);
    }



    private Boolean checkParam(Object... params) {
        for (Object param : params) {
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
