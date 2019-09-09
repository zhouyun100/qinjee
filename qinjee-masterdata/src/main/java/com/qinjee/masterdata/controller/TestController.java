package com.qinjee.masterdata.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月09日 11:30:00
 */
@RequestMapping("/test")
@RestController
@Api(description = "Set of endpoints for Creating, Retrieving, Updating and Deleting of Persons.")
public class TestController {

    @Value(value = "${spring.profiles.active}")
    String profiles;

    @ApiOperation("Returns list of all Persons in the system.")
    @GetMapping("/getProfiles")
    public Map<Object, Object> getProfiles(){
        Map<Object, Object> map = new HashMap<>();
        map.put("profiles",profiles);
        return map;
    }

}
