/**
 * 文件名：UserLogin
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/10
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.controller.userinfo;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.vo.auth.UserInfoVO;
import com.qinjee.masterdata.model.vo.userinfo.CompanyRegistParamVO;
import com.qinjee.masterdata.model.vo.userinfo.PhoneCodeParamVO;
import com.qinjee.masterdata.service.sms.SmsRecordService;
import com.qinjee.masterdata.service.userinfo.CompanyRegistService;
import com.qinjee.model.response.ResponseResult;
import com.qinjee.utils.RegexpUtils;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 周赟
 * @date 2020/01/03
 */
@Api(tags = "【注册接口】注册企业")
@RestController
@RequestMapping("/regist")
public class CompanyRegistController extends BaseController{

    private static Logger logger = LogManager.getLogger(CompanyRegistController.class);

    private ResponseResult responseResult;

    @Autowired
    private CompanyRegistService companyRegistService;

    @Autowired
    private SmsRecordService smsRecordService;

    @ApiOperation(value="发送企业注册手机号验证码", notes="根据手机号发送短信验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/sendRegistCodeByPhone",method = RequestMethod.GET)
    public ResponseResult sendCodeByMobile(String phone) {

        if(StringUtils.isEmpty(phone) || !RegexpUtils.checkPhone(phone)){
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("手机号错误，请重新输入!");
            return responseResult;
        }

        try{

            smsRecordService.sendSmsRegistCode(phone);
            logger.info("sendRegistCodeByPhone success！phone={}", phone);
            responseResult = ResponseResult.SUCCESS();
            responseResult.setMessage("手机号短信验证码发送完毕!");

        }catch (Exception e){
            logger.info("sendRegistCodeByPhone exception! phone={},exception={}", phone, e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("手机号短信验证码发送异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="校验注册手机号验证码", notes="校验手机号验证码是否正确以及手机号是否符合注册企业要求")
    @RequestMapping(value = "/checkPhoneCode",method = RequestMethod.POST)
    public ResponseResult checkPhoneCode(@RequestBody PhoneCodeParamVO phoneCodeParamVO) {

        try {

            if(StringUtils.isBlank(phoneCodeParamVO.getPhone()) || StringUtils.isBlank(phoneCodeParamVO.getCode()) || !RegexpUtils.checkPhone(phoneCodeParamVO.getPhone())){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("手机号或验证码错误，请重新输入!");
                return responseResult;
            }

            String redisPhoneLoginCode = redisClusterService.get("REGIST_" + phoneCodeParamVO.getPhone());
            if(StringUtils.isBlank(redisPhoneLoginCode) || !redisPhoneLoginCode.equals(phoneCodeParamVO.getCode())){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("手机号验证码无效!");
                return responseResult;
            }

            Integer registCount = companyRegistService.searchRegistCompanyCountByPhone(phoneCodeParamVO.getPhone());
            if(registCount != null && registCount > 0){
                responseResult = ResponseResult.FAIL();
                responseResult.setMessage("手机号已注册企业，不能重复注册!");
                return responseResult;
            }

            logger.info("checkPhoneCode success！phone={};", phoneCodeParamVO.getPhone());
            responseResult = ResponseResult.SUCCESS();
            responseResult.setMessage("手机号验证完毕!");

        }catch(Exception e) {
            logger.info("checkPhoneCode exception! phone={};code={};exception={}", phoneCodeParamVO.getPhone(),phoneCodeParamVO.getCode(),e.toString());
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
            responseResult.setMessage("手机号验证码校验异常！");
        }
        return responseResult;
    }

    @ApiOperation(value="企业注册", notes="默认创建有效期一年的企业级SaaS版")
    @RequestMapping(value = "/registCompany",method = RequestMethod.POST)
    public ResponseResult registCompany(@RequestBody CompanyRegistParamVO companyRegistParamVO) {

        try {

            companyRegistService.registCompany(companyRegistParamVO);
            responseResult = ResponseResult.SUCCESS();
        }catch(Exception e) {
            logger.info("registCompany exception! exception={}", e);
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
        }
        return responseResult;
    }


}
