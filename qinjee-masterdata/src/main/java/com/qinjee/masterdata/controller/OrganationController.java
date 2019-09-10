package com.qinjee.masterdata.controller;

import com.qinjee.masterdata.entity.Organation;
import com.qinjee.model.response.PageResult;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api("机构相关接口")
public class OrganationController {


    @GetMapping("/getOrganationList")
    @ApiOperation(value = "查询用户下所有的机构",notes = "高雄")
    public ResponseResult<PageResult<Organation>> getOrganationList(){

        return null;
    }



}
