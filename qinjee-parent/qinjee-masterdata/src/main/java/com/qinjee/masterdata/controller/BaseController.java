/**
 * 文件名：BaseController
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/9/10
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qinjee.consts.ResponseConsts;
import com.qinjee.masterdata.model.vo.auth.UserInfoVO;
import com.qinjee.masterdata.redis.RedisClusterService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 周赟
 * @date 2019/9/10
 */
@Component
public class BaseController {

    @Autowired
    protected RedisClusterService redisClusterService;

    @Autowired
    protected HttpServletRequest request;

    protected UserSession userSession;

    /**
     * 获取当前登录用户基本信息
     * @return
     */
    public UserSession getUserSession(){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (ResponseConsts.SESSION_KEY.equals(cookies[i].getName())) {
                    userSession = new UserSession();
                    JSONObject jsonObject = JSON.parseObject(redisClusterService.get(cookies[i].getValue()));
                    userSession.setUserId(Integer.valueOf(String.valueOf(jsonObject.get("userId"))));
                    userSession.setUserName(String.valueOf(jsonObject.get("userName")));
                    userSession.setPhone(String.valueOf(jsonObject.get("phone")));
                    userSession.setEmail(String.valueOf(jsonObject.get("email")));
                    userSession.setCompanyId(Integer.valueOf(String.valueOf(jsonObject.get("companyId"))));
                    userSession.setArchiveId(Integer.valueOf(String.valueOf(jsonObject.get("archiveId"))));

                    return userSession;
                }
            }
        }
        return null;
    }


    protected void setSessionAndCookie(HttpServletResponse response, UserInfoVO userInfo){
        StringBuffer loginKey = new StringBuffer();
        loginKey.append(ResponseConsts.SESSION_KEY).append("_").append(userInfo.getUserId());

        //设置redis登录缓存时间，120分钟过期，与前端保持一致
        redisClusterService.setex(loginKey.toString(), ResponseConsts.SESSION_INVALID_SECOND, JSON.toJSONString(userInfo));
        Cookie cookie = new Cookie(ResponseConsts.SESSION_KEY, loginKey.toString());
        cookie.setMaxAge(ResponseConsts.SESSION_INVALID_SECOND);
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    public static ResponseResult getResponseResult(Integer integer, String message){
        if(integer>1){
            return ResponseResult.SUCCESS();
        }
        ResponseResult fail = ResponseResult.FAIL();
        fail.setMessage(message);
        return fail;
    }

}
