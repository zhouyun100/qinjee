package com.qinjee.masterdata.controller.StaffController;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/staff")
@Api("人员相关接口")
public class StaffPreEmploymentController extends BaseController {
   public ResponseResult insertPreEmploymentList(){
       return null;
   }
}