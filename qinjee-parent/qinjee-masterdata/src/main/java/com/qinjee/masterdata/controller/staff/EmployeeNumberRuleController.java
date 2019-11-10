package com.qinjee.masterdata.controller.staff;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.vo.staff.EmployeeNumberRuleVo;
import com.qinjee.masterdata.service.staff.EmployeeNumberRuleService;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月25日 09:43:00
 */
@RestController
@RequestMapping("/employeeNumberRule")
@Api(tags = "【人员管理】工号规则接口")
public class EmployeeNumberRuleController extends BaseController {
    @Autowired
    private EmployeeNumberRuleService employeeNumberRuleService;

    @PostMapping("/addEmployeeNumberRule")
    @ApiOperation(value = "新增员工规则", notes = "高雄")
    public ResponseResult addEmployeeNumberRule(EmployeeNumberRuleVo employeeNumberRuleVo){
      checkParam(employeeNumberRuleVo,getUserSession());
        try {
            return employeeNumberRuleService.addEmployeeNumberRule(employeeNumberRuleVo, getUserSession());
        } catch (Exception e) {
            e.printStackTrace();
            return failResponseResult("添加工号规则失败");
        }
    }
    //根据工号规则生成工号
    @RequestMapping(value = "/createEmployeeNumber" ,method = RequestMethod.POST)
    @ApiOperation(value = "根据工号规则生成工号", notes = "hkt")
    public ResponseResult createEmployeeNumber(Integer id) throws Exception {
        checkParam(id);
        try {
            return employeeNumberRuleService.createEmployeeNumber(id,getUserSession());
        } catch (Exception e) {
            e.printStackTrace();
            return failResponseResult("生成工号失败");
        }
    }
    private Boolean checkParam(Object... params) {
        for (Object param : params) {
            if (null == param || "".equals(param)) {
                return false;
            }
        }
        return true;
    }

    private ResponseResult failResponseResult(String message){
        ResponseResult fail = ResponseResult.FAIL();
        fail.setMessage(message);
        return fail;
    }
}
