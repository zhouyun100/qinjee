package com.qinjee.masterdata.controller.staff;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.entity.Blacklist;
import com.qinjee.masterdata.model.entity.PreEmploymentChange;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/staffpre")
@Api(tags = "【人员管理】预入职相关接口")
public class StaffPreEmploymentController extends BaseController {


    /**
     * 发短信给预入职人员
     */
    @RequestMapping(value = "/sendMessage ", method = RequestMethod.GET)
    @ApiOperation(value = "短信发送入职登记表", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "预入职登记表id", paramType = "query", required = true)
    public ResponseResult sendMessage(Integer[] id) {
        ResponseResult listResponseResult = new ResponseResult<>(CommonCode.SUCCESS);
        return listResponseResult;
    }

    /**
     * 发邮件给预入职人员
     */
    @RequestMapping(value = "/sendMail ", method = RequestMethod.GET)
    @ApiOperation(value = "邮箱发送入职登记表", notes = "hkt")
    @ApiImplicitParam(name = "id", value = "预入职登记表id", paramType = "query", required = true)
    public ResponseResult sendMail(Integer[] id) {
        ResponseResult listResponseResult = new ResponseResult<>(CommonCode.SUCCESS);
        return listResponseResult;
    }

    /**
     * 校验手机号码
     */
    @RequestMapping(value = "/checkPhone ", method = RequestMethod.GET)
    @ApiOperation(value = "校验手机号码", notes = "hkt")
    @ApiImplicitParam(name = "phoneNumber", value = "手机号", paramType = "query", required = true)
    public ResponseResult checkPhone(String phoneNumber) {
        ResponseResult<Boolean> booleanResponseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return booleanResponseResult;
    }

    /**
     * 发送验证码
     */

    @RequestMapping(value = "/getPhoneCheckNumber ", method = RequestMethod.GET)
    @ApiOperation(value = "手机验证码", notes = "hkt")
    @ApiImplicitParam(name = "phoneNumber", value = "手机号", paramType = "query", required = true)
    public ResponseResult getPhoneCheckNumber(String phoneNumber) {
        String checknumber = "手机验证码";
        ResponseResult<String> stringResponseResult = new ResponseResult<>(checknumber, CommonCode.SUCCESS);
        return stringResponseResult;
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
    @ApiImplicitParam(name = "jsonObject", value = "预入职变更表", paramType = "query", required = true)
    public ResponseResult update(PreEmploymentChange preEmploymentChange) {
        ResponseResult<Boolean> responseResult = new ResponseResult<>(true, CommonCode.SUCCESS);
        return responseResult;
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
