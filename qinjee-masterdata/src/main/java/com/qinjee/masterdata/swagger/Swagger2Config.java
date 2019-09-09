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
@Profile({"dev","test"})
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .select()
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
