package com.qinjee.masterdata.controller.staff;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.vo.staff.EmployeeNumberRuleVo;
import com.qinjee.masterdata.service.staff.EmployeeNumberRuleService;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.prefs.BackingStoreException;

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

    @RequestMapping("/addEmployeeNumberRule")
    @ApiOperation(value = "新增员工规则", notes = "高雄")
    public ResponseResult addEmployeeNumberRule(EmployeeNumberRuleVo employeeNumberRuleVo){
        return employeeNumberRuleService.addEmployeeNumberRule(employeeNumberRuleVo, getUserSession());
    }

}
