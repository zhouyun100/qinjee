/*
 * 文件名： ApiFallbackProvider.java
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.qinjee.consts.ResponseConsts;
import com.qinjee.entity.ResultJsonEntity;

/**
 * 熔断，当服务down掉后，会默认返回getStatusText（）中的提示信息
 *
 * @author 周赟
 *
 * @version
 *
 * @since 2019年5月25日
 */
@Component
public class ApiFallbackProvider implements FallbackProvider {

	@Override
	public String getRoute() {
		return "*";
	}

	@Override
	public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
		return new ClientHttpResponse() {

			@Override
			public InputStream getBody() throws IOException {
				return new ByteArrayInputStream(getStatusText().getBytes());
			}

			@Override
			public HttpHeaders getHeaders() {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				return headers;
			}

			@Override
			public HttpStatus getStatusCode() throws IOException {
				return HttpStatus.OK;
			}

			@Override
			public int getRawStatusCode() throws IOException {
				return 200;
			}

			@Override
			public String getStatusText() throws IOException {
				ResultJsonEntity resultJson = new ResultJsonEntity();
				resultJson.setResultCode(ResponseConsts.RESULT_CODE_NET_EXCEPTION);
				resultJson.setResultStatus(ResponseConsts.RESULT_STATUS_NET_EXCEPTION);
				resultJson.setResult(ResponseConsts.RESULT_RESULT_NET_MESSAGE);
				String result = JSON.toJSONString(resultJson);
				return result;
			}

			@Override
			public void close() {

			}

		};
	}

}
