/*
 * 文件名： AuthFilter.java
 *
 * 工程名称: qinjee-zuul
 *
 * Qinjee
 *
 * 创建日期： 2019年5月25日
 *
 * Copyright(C) 2019, by zhouyun
 *
 * 原始作者: 周赟
 *
 */
package com.qinjee.api.filter;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.qinjee.consts.ResponseConsts;
import com.qinjee.entity.ResultJsonEntity;
import com.qinjee.zull.redis.RedisService;

/**
 *
 *
 * @author 周赟
 *
 * @version
 *
 * @since 2019年5月25日
 */
@Component
public class AuthFilter extends ZuulFilter{

	@Autowired
	private RedisService redisService;

	//排除过滤的 uri 地址
    private static final String LOGIN_URI = "/api/tsc/user/login";
    private static final String REGISTER_URI = "/api/tsc/user/register";

	// 每秒产生1000个令牌
	private static final RateLimiter RATE_LIMITER = RateLimiter.create(1000);

	@Override
	public boolean shouldFilter() {
		RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        String requestUri = request.getRequestURI();
        //注册和登录接口不拦截，其他接口都要拦截校验 token
        if (LOGIN_URI.equals(requestUri) || REGISTER_URI.equals(requestUri)) {
            return false;
        }
        return true;
	}


	@Override
	public Object run(){
		RequestContext requestContext = RequestContext.getCurrentContext();

		//就相当于每调用一次tryAcquire()方法，令牌数量减1，当1000个用完后，那么后面进来的用户无法访问上面接口
        //当然这里只写类上面一个接口，可以这么写，实际可以在这里要加一层接口判断。
        if (!RATE_LIMITER.tryAcquire()) {
            requestContext.setSendZuulResponse(false);
            //HttpStatus.TOO_MANY_REQUESTS.value()里面有静态代码常量
            requestContext.setResponseStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
            return null;
        }

        HttpServletRequest request = requestContext.getRequest();

        //token对象,有可能在请求头传递过来，也有可能是通过参数传过来，实际开发一般都是请求头方式
//        String token = request.getHeader("token");
//        if (StringUtils.isEmpty((token))) {
//            token = request.getParameter("token");
//        }
//        if (StringUtils.isEmpty((token))) {
//        	setUnauthorizedResponse(requestContext);
//        }

        //先从 cookie 中取 SESSION_KEY
        Cookie sessionKey = getCookie(request, ResponseConsts.SESSION_KEY);
        if (sessionKey == null || StringUtils.isEmpty(sessionKey.getValue())) {
        	setUnauthorizedResponse(requestContext);
        }else {
			if (redisService.exists(sessionKey.getValue())) {
				/**
				 * 判断登录session失效时间，小于5分钟则重新更换session时长
				 */
				Long loginKeyValidTime = redisService.getExpire(sessionKey.getValue());
				if (loginKeyValidTime < ResponseConsts.SESSION_CHECK_SECCOND) {
					redisService.expire(sessionKey.getValue(), ResponseConsts.SESSION_INVALID_SECCOND);
				}
			}else {
				setUnauthorizedResponse(requestContext);
			}
        }

		return null;
	}


	@Override
	public String filterType() {
		return PRE_TYPE;
	}


	@Override
	public int filterOrder() {
		return PRE_DECORATION_FILTER_ORDER - 1;
	}

	/**
	 *
	 * 功能描述：设置 401 无权限状态
	 *
	 * @param requestContext
	 *
	 * @author 周赟
	 *
	 * @since 2019年5月25日
	 *
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
    private void setUnauthorizedResponse(RequestContext requestContext){
    	requestContext.setSendZuulResponse(false);
        requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        String result = JSON.toJSONString(new ResponseResult(CommonCode.INVALID_SESSION));
        requestContext.setResponseBody(result);
    }

	public Cookie getCookie(HttpServletRequest request,String cookieName) {
		Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookieName.equals(cookies[i].getName())) {
					return cookies[i];
				}
			}
		}
		return null;
	}
}
