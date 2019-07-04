/*
 * 文件名： Swagger2.java
 * 
 * 工程名称: qinjee-tsc
 *
 * Qinjee
 *
 * 创建日期： 2019年6月3日
 *
 * Copyright(C) 2019, by zhouyun
 *
 * 原始作者: 周赟
 *
 */
package com.qinjee.tsc.swagger;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 *
 * @author 周赟
 *
 * @version 
 *
 * @since 2019年6月3日
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

	//是否开启swagger，正式环境一般是需要关闭的，可根据springboot的多环境配置进行设置
    @Value(value = "${spring.profiles.active}")
    String profiles;
 
    @Bean
    public Docket createRestApi() {
    	Boolean swaggerEnabled = false;
    	if(StringUtils.isNotBlank(profiles) && !"prod".equals(profiles)) {
    		swaggerEnabled = true;
    	}
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                // 是否开启
                .enable(swaggerEnabled).select()
                // 扫描的路径包
                .apis(RequestHandlerSelectors.basePackage("com.qinjee.tsc.controller"))
                // 指定路径处理PathSelectors.any()代表所有的路径
                .paths(PathSelectors.any()).build().pathMapping("/");
    }
 
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("tsc接口文档")
                .description("管理员：周赟 | zhouyun@qinjee.cn")
                .contact(new Contact("周赟","https://github.com/orgs/qinjeecode/teams/tsc_team","zhouyun@qinjee.cn"))
                .version("1.0.0")
                .build();
    }
}
