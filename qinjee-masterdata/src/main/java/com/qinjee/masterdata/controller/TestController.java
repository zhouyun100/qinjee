package com.qinjee.masterdata.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月09日 11:30:00
 */
@RequestMapping("/test")
@RestController
public class TestController {

    @Value(value = "${spring.profiles.active}")
    String profiles;

    @PostConstruct
    public void init(){
        System.out.println("profiles:" + profiles);
    }

    @GetMapping("/getProfiles")
    public String getProfiles(){
        return profiles;
    }

}
