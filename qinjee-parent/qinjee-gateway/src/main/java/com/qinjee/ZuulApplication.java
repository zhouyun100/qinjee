/*
 * 文件名： ZuulApplication.java
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
package com.qinjee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * zuul网关
 *
 * @author 周赟
 * @date 2019/10/15
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableEurekaClient
@EnableZuulProxy
public class ZuulApplication{

	/**
	 * 功能描述：
	 *
	 * @param args
	 * 
	 * @author 周赟
	 *
	 * @since 2019年5月25日
	 *
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static void main(String[] args) {
		SpringApplication.run(ZuulApplication.class, args);
	}

}
