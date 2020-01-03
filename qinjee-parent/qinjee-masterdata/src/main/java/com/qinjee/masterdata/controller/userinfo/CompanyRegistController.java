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
import com.qinjee.masterdata.service.userinfo.CompanyRegistService;
import com.qinjee.model.response.ResponseResult;
import com.qinjee.utils.RegexpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author 周赟
 * @date 2020/01/03
 */
@Api(tags = "【注册接口】")
@RestController
@RequestMapping("/regist")
public class CompanyRegistController extends BaseController{

    private static Logger logger = LogManager.getLogger(CompanyRegistController.class);

    private ResponseResult responseResult;

    @Autowired
    private CompanyRegistService companyRegistService;

    @ApiOperation(value="企业注册", notes="默认创建供10000人使用的企业级SaaS版")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyName", value = "企业名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "注册人员手机号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "account", value = "注册人登录账号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/registCompany",method = RequestMethod.POST)
    public ResponseResult registCompany(String companyName, String phone, String account) {

        try {
            Integer userNumber = 10000;
            //增加一年
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.YEAR, 1);
            Date validEndDate = cal.getTime();

            companyRegistService.registCompany(companyName,userNumber,validEndDate,phone,account);
            responseResult = ResponseResult.SUCCESS();
        }catch(Exception e) {
            logger.info("registCompany exception! exception={}", e);
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
        }
        return responseResult;
    }

}
