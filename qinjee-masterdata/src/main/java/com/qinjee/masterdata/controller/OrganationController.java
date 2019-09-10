package com.qinjee.masterdata.controller;

import com.qinjee.masterdata.entity.Organation;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description 机构controller
 * @createTime 2019年09月10日 10:19:00
 */
@RequestMapping("/organation")
@RestController
@Api("机构controller")
public class OrganationController {


    @GetMapping("/getTOrganationList")
    public ResponseResult<Organation> getTOrganationList(){


        return null;
    }

}
