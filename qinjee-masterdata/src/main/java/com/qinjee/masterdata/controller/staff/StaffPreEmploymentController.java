package com.qinjee.masterdata.controller.staff;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.Blacklist;
import com.qinjee.masterdata.model.vo.staff.StatusChange;
import com.qinjee.masterdata.service.staff.IStaffPreEmploymentService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/staffpre")
@Api(tags = "【人员管理】预入职相关接口")
public class StaffPreEmploymentController extends BaseController {
    @Autowired
    private IStaffPreEmploymentService staffPreEmploymentService;


    /**
     * 发短信给预入职人员
     */
    @RequestMapping(value = "/sendMessage ", method = RequestMethod.GET)
    @ApiOperation(value = "短信发送入职登记表", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "list", value = "预入职登记表id组成的集合", paramType = "query", required = true),
            @ApiImplicitParam(name = "templateId", value = "短信模板", paramType = "query", required = true),
            @ApiImplicitParam(name = "params[]", value = "所传参数", paramType = "query", required = true),

    })
    public ResponseResult sendMessage(List<Integer> list,Integer templateId,String[] params) {
       return staffPreEmploymentService.sendMessage(list,templateId,params);
    }

    /**
     * 发邮件给预入职人员
     */
    @RequestMapping(value = "/sendMail ", method = RequestMethod.GET)
    @ApiOperation(value = "邮箱发送入职登记表", notes = "hkt")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "list", value = "预入职收信人id集合", paramType = "query", required = true),
            @ApiImplicitParam(name = "list", value = "抄送人档案id集合", paramType = "query", required = true),
            @ApiImplicitParam(name = "String", value = "发送邮件主题", paramType = "query", required = true),
            @ApiImplicitParam(name = "String", value = "发送邮件内容", paramType = "query", required = true),
            @ApiImplicitParam(name = "String[]", value = "发送邮件附件路径的数组", paramType = "query", required = true),
    })
    public ResponseResult sendMail(List<Integer> prelist,List<Integer> conList,String content,String subject,String[] filepath) {
        return staffPreEmploymentService.sendManyMail(prelist,conList,content,subject,filepath) ;
    }

    /**
     * 校验手机号码
     */
    @RequestMapping(value = "/checkPhone ", method = RequestMethod.GET)
    @ApiOperation(value = "校验手机号码", notes = "hkt")
    @ApiImplicitParam(name = "String", value = "手机号", paramType = "query", required = true)
    public ResponseResult checkPhone(String phoneNumber) {
        return  staffPreEmploymentService.checkPhone(phoneNumber);
    }

    /**
     * 校验邮箱
     */
    @RequestMapping(value = "/checkMail ", method = RequestMethod.GET)
    @ApiOperation(value = "校验邮箱", notes = "hkt")
    @ApiImplicitParam(name = "String", value = "邮箱", paramType = "query", required = true)
    public ResponseResult checkMail(String mail) {
        return  staffPreEmploymentService.checkMail(mail);
    }



    /**
     * 生成入职登记二维码,返回是二维码图片链接
     */
    @RequestMapping(value = "/creatQrcodeForPre", method = RequestMethod.GET)
    @ApiOperation(value = "生成入职登记二维码", notes = "hkt")
    @ApiImplicitParam(name = "url", value = "链接地址", paramType = "query", required = true)
    public ResponseResult creatQrcodeForPre(String url) {

        ResponseResult<String> stringResponseResult = new ResponseResult<>("qrcode path", CommonCode.SUCCESS);
        return stringResponseResult;
    }

    /**
     * 延期入职以及放弃入职(延期入职时间还需要添加时间)
     * 延期入职需要更新预入职信息的入职时间，同时更新预入职更改表
     */

    @RequestMapping(value = "/updatePreEmploymentChange", method = RequestMethod.POST)
    @ApiOperation(value = "延期入职以及放弃入职", notes = "hkt")
    @ApiImplicitParam(name = "StatusChange", value = "预入职变更表vo类", paramType = "query", required = true)
    public ResponseResult updatePreEmploymentChange(StatusChange statusChange) {
       return staffPreEmploymentService.insertStatusChange(statusChange);
    }

    /**
     * 加入黑名单表
     */
    @RequestMapping(value = "/insertBalckList", method = RequestMethod.POST)
    @ApiOperation(value = "加入黑名单表", notes = "hkt")
    @ApiImplicitParam(name = "blackList", value = "黑名单表", paramType = "query", required = true)
    public ResponseResult update(Blacklist blacklist) {
        ResponseResult<Boolean> responseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return responseResult;
    }
}
