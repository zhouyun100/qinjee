package com.qinjee.masterdata.controller.numberandparam;

import com.qinjee.masterdata.controller.BaseController;
import com.qinjee.masterdata.model.vo.staff.ContractParamVo;
import com.qinjee.masterdata.model.vo.staff.CreatNumberVo;
import com.qinjee.masterdata.model.vo.staff.EmployeeNumberRuleVo;
import com.qinjee.masterdata.service.employeenumberrule.IEmployeeNumberRuleService;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/number")
@Api(tags = "工号合同编号操作")
public class NumberController extends BaseController {
    @Autowired
    private IEmployeeNumberRuleService employeeNumberRuleService;
    /**
     * 新增合同参数表
     */
    @RequestMapping(value = "/insertContractParam", method = RequestMethod.POST)
    @ApiOperation(value = "新增合同参数表", notes = "hkt")
//    @ApiImplicitParam(name = "customArchiveTable", value = "自定义表", paramType = "form" ,required = true)
    public ResponseResult insertContractParam(@Valid ContractParamVo contractParamVo) {
        Boolean b = checkParam(contractParamVo, getUserSession());
        if (b) {
            try {
                employeeNumberRuleService.addContractParam(contractParamVo,getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("新增合同参数表失败");
            }
        }
        return failResponseResult("合同参数表参数错误或者session错误");
    }
    /**
     * 新增工号生成表
     */
    @RequestMapping(value = "/insertEmployNumber", method = RequestMethod.POST)
    @ApiOperation(value = "新增工号生成表", notes = "hkt")
//    @ApiImplicitParam(name = "customArchiveTable", value = "自定义表", paramType = "form" ,required = true)
    public ResponseResult insertEmployNumber(@Valid EmployeeNumberRuleVo employeeNumberRuleVo) {
        Boolean b = checkParam(employeeNumberRuleVo, getUserSession());
        if (b) {
            try {
                employeeNumberRuleService.addEmployeeNumberRule(employeeNumberRuleVo,getUserSession());
                return ResponseResult.SUCCESS();
            } catch (Exception e) {
                return failResponseResult("新增工号表失败");
            }
        }
        return failResponseResult("工号表错误或者session错误");
    }
    /**
     * 生成工号或者合同编号
     */
    @RequestMapping(value = "/creatNumber", method = RequestMethod.POST)
    @ApiOperation(value = "生成工号或者合同编号", notes = "hkt")
//    @ApiImplicitParam(name = "customArchiveTable", value = "自定义表", paramType = "form" ,required = true)
    public ResponseResult<String> creatNumber(@Valid CreatNumberVo creatNumberVo) {
        Boolean b = checkParam(creatNumberVo, getUserSession());
        if (b) {
            try {
                String number = employeeNumberRuleService.createNumber(creatNumberVo, getUserSession());
                return new ResponseResult<>(number, CommonCode.SUCCESS);
            } catch (Exception e) {
                return failResponseResult("生成工号或者合同编号失败");
            }
        }
        return failResponseResult("生成工号或者合同编号错误或者session错误");
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
