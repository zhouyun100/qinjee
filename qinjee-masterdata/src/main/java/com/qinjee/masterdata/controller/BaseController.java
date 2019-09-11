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

import com.qinjee.model.request.UserSession;

import javax.servlet.http.HttpServletRequest;

//@Component
public class BaseController {

//    @Autowired
//    private RedisClusterService redisClusterService;

    public UserSession userSession;


    /**
     * 获取当前登录用户基本信息
     * @param request
     * @return
     */
    public UserSession getUserSession(HttpServletRequest request){
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (int i = 0; i < cookies.length; i++) {
//                if (ResponseConsts.SESSION_KEY.equals(cookies[i].getName())) {
//                    userSession = new UserSession();
//
//                    JSONObject jsonObject = JSON.parseObject(redisClusterService.get(cookies[i].getValue()));
//                    userSession.setUserId(Integer.valueOf(String.valueOf(jsonObject.get("userId"))));
//                    userSession.setUserName(String.valueOf(jsonObject.get("userName")));
//                    userSession.setPhone(String.valueOf(jsonObject.get("phone")));
//                    userSession.setEmail(String.valueOf(jsonObject.get("email")));
//                    userSession.setCompanyId(Integer.valueOf(String.valueOf(jsonObject.get("companyId"))));
//                    userSession.setArchiveId(Integer.valueOf(String.valueOf(jsonObject.get("archiveId"))));
//
//                    return userSession;
//                }
//            }
//        }
        return null;
    }
}
