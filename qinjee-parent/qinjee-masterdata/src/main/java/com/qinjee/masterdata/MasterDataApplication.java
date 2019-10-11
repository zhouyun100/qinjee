package com.qinjee.masterdata;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月09日 10:01:00
 */
@SpringBootApplication
@EnableEurekaClient
@EnableTransactionManagement
@EnableSwagger2
@MapperScan(basePackages = {"com.qinjee.masterdata.dao"})
@ComponentScan(basePackages = {"com.qinjee.config", "com.qinjee.exception","com.qinjee.masterdata"})
public class MasterDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(MasterDataApplication.class, args);
    }

}
