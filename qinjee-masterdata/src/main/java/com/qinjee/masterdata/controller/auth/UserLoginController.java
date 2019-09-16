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

import com.alibaba.fastjson.JSON;
import com.qinjee.consts.ResponseConsts;
import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.vo.auth.MenuVO;
import com.qinjee.masterdata.model.vo.auth.UserInfoVO;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 周赟
 * @date 2019/9/10
 */
@Api(tags = "【登录注册】用户登录接口")
@RestController
@RequestMapping("/userLogin")
public class UserLoginController extends BaseController{

    private static Logger logger = LogManager.getLogger(UserLoginController.class);

    private ResponseResult responseResult;

    @ApiOperation(value="手机号验证码登录", notes="根据手机号、验证码来登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String")
    })
    @RequestMapping(value = "/loginByPhoneAndCode",method = RequestMethod.GET)
    public ResponseResult<UserInfoVO> loginByMobileAndCode(HttpServletResponse response, String phone, String code) {

        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setUserId(1);
        userInfo.setUserName("zhouyun");
        userInfo.setPhone("18612406236");
        userInfo.setEmail("zhouyun@qinjee.cn");
        userInfo.setCompanyId(1);
        userInfo.setArchiveId(1);

        try {

//            userInfo = userInfoService.selectByUsernameAndPassword(userInfo);
            if (userInfo != null) {
                String loginKey = ResponseConsts.SESSION_KEY + userInfo.getUserId();
                /**
                 * 设置redis登录缓存时间，30分钟过期，与前端保持一致
                 */
                redisClusterService.setex(loginKey, ResponseConsts.SESSION_INVALID_SECCOND, JSON.toJSONString(userInfo));
                Cookie cookie = new Cookie(ResponseConsts.SESSION_KEY, loginKey);
                cookie.setMaxAge(ResponseConsts.SESSION_INVALID_SECCOND);
                cookie.setPath("/");
                response.addCookie(cookie);

                logger.info("Login success！phone={};code={}", phone,code);
                responseResult = ResponseResult.SUCCESS();
                responseResult.setResult(userInfo);
            }else {
                logger.info("Login faild,No this phone found! phone={};code={}", phone,code);

                responseResult = ResponseResult.FAIL();
            }

        }catch(Exception e) {
            logger.info("Login exception! phone={};code={}", phone,code);
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
        }
        return responseResult;
    }

    @ApiOperation(value="账号密码登录", notes="根据用户名/手机号/邮箱、密码来登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "用户名/手机号/邮箱", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String")
    })
    @RequestMapping(value = "/loginByAccountAndPassword",method = RequestMethod.GET)
    public ResponseResult<UserSession> loginByAccountAndPassword(HttpServletResponse response, String account, String password) {

        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setUserId(1);
        userInfo.setUserName("zhouyun");
        userInfo.setPhone("18612406236");
        userInfo.setEmail("zhouyun@qinjee.cn");
        userInfo.setCompanyId(1);
        userInfo.setArchiveId(1);

        try {

//            userInfo = userInfoService.selectByUsernameAndPassword(userInfo);

            if (userInfo != null) {
                String loginKey = ResponseConsts.SESSION_KEY + userInfo.getUserId();
                /**
                 * 设置redis登录缓存时间，30分钟过期，与前端保持一致
                 */
                redisClusterService.setex(loginKey, ResponseConsts.SESSION_INVALID_SECCOND, JSON.toJSONString(userInfo));
                Cookie cookie = new Cookie(ResponseConsts.SESSION_KEY, loginKey);
                cookie.setMaxAge(ResponseConsts.SESSION_INVALID_SECCOND);
                cookie.setPath("/");
                response.addCookie(cookie);

                logger.info("Login success！account={};password={}", account,password);
                responseResult = ResponseResult.SUCCESS();
                responseResult.setResult(userInfo);
            }else {
                logger.info("Login faild,No this phone found! account={};password={}", account,password);

                responseResult = ResponseResult.FAIL();
            }

        }catch(Exception e) {
            logger.info("Login exception! account={};password={}", account,password);
            e.printStackTrace();
            responseResult = ResponseResult.FAIL();
        }
        return responseResult;

    }

    @ApiOperation(value="发送短信验证码", notes="根据手机号发送短信验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String")
    })
    @RequestMapping(value = "/sendCodeByPhone",method = RequestMethod.GET)
    public ResponseResult sendCodeByMobile(String phone) {

        return null;
    }

    @ApiOperation(value="修改密码", notes="修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "用户名/手机号/邮箱", required = true, dataType = "String"),
            @ApiImplicitParam(name = "oldPassword", value = "旧密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = true, dataType = "String")
    })
    @RequestMapping(value = "/updatePasswordByAccount",method = RequestMethod.GET)
    public ResponseResult<UserSession> updatePasswordByAccount(String account, String oldPassword, String newPassword) {

        return null;
    }


    @ApiOperation(value="加载当前登录用户菜单树", notes="加载当前登录用户菜单树")
    @RequestMapping(value = "/loadMenuTreeByCurrentLoginUser",method = RequestMethod.GET)
    public ResponseResult<MenuVO> loadMenuTreeByCurrentLoginUser() {
        userSession = getUserSession();
        logger.info("loadMenuTreeByCurrentLoginUser！archiveId={} ", userSession.getArchiveId());

        responseResult = ResponseResult.SUCCESS();
        return responseResult;
    }

    @ApiOperation(value="退出", notes="退出")
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public ResponseResult logout(HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (ResponseConsts.SESSION_KEY.equals(cookies[i].getName())) {
                    String loginKey = cookies[i].getValue();
                    if (loginKey != null) {
                        if(redisClusterService.exists(loginKey)) {
                            redisClusterService.del(loginKey);
                        }
                        cookies[i].setValue(null);
                        // 立即销毁cookie
                        cookies[i].setMaxAge(0);
                        cookies[i].setPath("/");
                        response.addCookie(cookies[i]);
                    }
                }
            }
        }

        responseResult = ResponseResult.SUCCESS();
        return responseResult;
    }

}
