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

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.qinjee.consts.ResponseConsts;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.ResponseResult;
import com.qinjee.zull.redis.RedisClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

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
	protected RedisClusterService redisClusterService;

	/**
	 * 非拦截地址
	 */
	private List<String> paths;

	public AuthFilter() {
		super();
		paths = new ArrayList<>();
		paths.add("/api/masterdata/userLogin/loginByAccountAndPassword");
		paths.add("/api/masterdata/userLogin/loginByPhoneAndCode");
		paths.add("/api/masterdata/userLogin/searchUserInfoByUserIdAndCompanyId");
		paths.add("/api/masterdata/userLogin/sendCodeByPhone");
		paths.add("/api/masterdata/userLogin/verifyCode");
		paths.add("/ui/**");
		paths.add("/**/swagger**/**");
		paths.add("/**/v2/api-docs");
		paths.add("/**/*.css");
		paths.add("/**/*.jpg");
		paths.add("/**/*.png");
		paths.add("/**/*.gif");
		paths.add("/**/*.js");
		paths.add("/**/*.svg");
	}

	/**
	 * 每秒产生1000个令牌
	 */
	private static final RateLimiter RATE_LIMITER = RateLimiter.create(1000);

	@Override
	public boolean shouldFilter() {
		RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        String requestUri = request.getRequestURI();
        /**
         * 注册和登录接口不拦截，其他接口都要拦截校验 token
		 */
		PathMatcher matcher = new AntPathMatcher();
		Optional<String> optional = paths.stream().filter(t -> matcher.match(t, requestUri)).findFirst();

		return !optional.isPresent();
	}


	@Override
	public Object run(){
		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletResponse response = requestContext.getResponse();
		response.setContentType("application/json;charset=utf-8");

		/**
		 * 就相当于每调用一次tryAcquire()方法，令牌数量减1，当1000个用完后，那么后面进来的用户无法访问上面接口
		 * 当然这里只写类上面一个接口，可以这么写，实际可以在这里要加一层接口判断。
		 */
        if (!RATE_LIMITER.tryAcquire()) {
            requestContext.setSendZuulResponse(false);

            //HttpStatus.TOO_MANY_REQUESTS.value()里面有静态代码常量
            requestContext.setResponseStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
            return null;
        }

        HttpServletRequest request = requestContext.getRequest();

		//先从 cookie 中取 SESSION_KEY
		Cookie sessionKey = getCookie(request, ResponseConsts.SESSION_KEY);
        if (sessionKey == null || StringUtils.isEmpty(sessionKey.getValue())) {
        	setUnauthorizedResponse(requestContext);
        }else {
			if (redisClusterService.exists(sessionKey.getValue())) {

				//判断登录session失效时间，小于30分钟则重新更换session时长
				Long loginKeyValidTime = redisClusterService.getExpire(sessionKey.getValue());
				if (loginKeyValidTime < ResponseConsts.SESSION_CHECK_SECOND) {
					redisClusterService.expire(sessionKey.getValue(), ResponseConsts.SESSION_INVALID_SECOND);
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
		Cookie[] cookies = request.getCookies();
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
