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
package com.qinjee.masterdata.controller.auth;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.vo.auth.MenuVO;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 周赟
 * @date 2019/9/10
 */
@Api(tags = "【登录注册】用户登录接口")
@RestController
@RequestMapping("/userLogin")
public class UserLoginController extends BaseController{

//    private static Logger logger = LogManager.getLogger(UserLoginController.class);

    @ApiOperation(value="手机号验证码登录", notes="根据手机号、验证码来登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String")
    })
    @RequestMapping(value = "/loginByPhoneAndCode",method = RequestMethod.GET)
    public ResponseResult<UserSession> loginByMobileAndCode(HttpServletRequest request, String phone, String code) {

        return null;
    }

    @ApiOperation(value="账号密码登录", notes="根据用户名/手机号/邮箱、密码来登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String")
    })
    @RequestMapping(value = "/loginByAccountAndPassword",method = RequestMethod.GET)
    public ResponseResult<UserSession> loginByAccountAndPassword(HttpServletRequest request, String account, String password) {

        return null;
    }

    @ApiOperation(value="发送短信验证码", notes="根据手机号发送短信验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/sendCodeByPhone",method = RequestMethod.GET)
    public ResponseResult sendCodeByMobile(HttpServletRequest request, String phone) {

        return null;
    }

    @ApiOperation(value="修改密码", notes="修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "用户名/手机号/邮箱", required = true, dataType = "String"),
            @ApiImplicitParam(name = "oldPassword", value = "旧密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = true, dataType = "String")
    })
    @RequestMapping(value = "/updatePasswordByAccount",method = RequestMethod.GET)
    public ResponseResult<UserSession> updatePasswordByAccount(HttpServletRequest request, String account, String oldPassword, String newPassword) {

        return null;
    }


    @ApiOperation(value="加载当前登录用户菜单树", notes="加载当前登录用户菜单树")
    @RequestMapping(value = "/loadMenuTreeByCurrentLoginUser",method = RequestMethod.GET)
    public ResponseResult<MenuVO> loadMenuTreeByCurrentLoginUser(HttpServletRequest request) {

        return null;
    }

}
