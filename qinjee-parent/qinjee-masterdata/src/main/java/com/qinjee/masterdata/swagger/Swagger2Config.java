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
package com.qinjee.masterdata.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.TagsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

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
@Profile({"dev", "test"})
public class Swagger2Config {

    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.qinjee.masterdata.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "masterdata接口文档",
                "管理员：周赟 | zhouyun@qinjee.cn",
                "v1.0.0",
                "http://192.168.1.178:8080/swagger-ui.html#/",
                new Contact("周赟","https://github.com/orgs/qinjeecode/teams/tsc_team","zhouyun@qinjee.cn"),
                "勤杰官网",
                "http://www.qinjee.cn/",
                Collections.emptyList()
        );
    }
}
