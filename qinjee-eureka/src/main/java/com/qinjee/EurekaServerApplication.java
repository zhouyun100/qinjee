/*
 * 文件名： EurekaServerApplication.java
 * 
 * 工程名称: qinjee-eureka-server
 *
 * Shang De
 *
 * 创建日期： 2019年5月15日
 *
 * Copyright(C) 2019, by zhouyun
 *
 * 原始作者: 周赟
 *
 */
package com.qinjee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * spring cloud eureka regist center
 *
 * @author 周赟
 *
 * @version
 *
 * @since 2019年5月15日
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
	}
}
