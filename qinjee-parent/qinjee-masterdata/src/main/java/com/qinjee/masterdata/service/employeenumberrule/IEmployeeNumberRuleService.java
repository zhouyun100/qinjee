package com.qinjee.masterdata.service.employeenumberrule;

import com.qinjee.masterdata.model.vo.staff.ContractParamVo;
import com.qinjee.masterdata.model.vo.staff.CreatNumberVo;
import com.qinjee.masterdata.model.vo.staff.EmployeeNumberRuleVo;
import com.qinjee.model.request.UserSession;

public interface IEmployeeNumberRuleService {
    /**
     * 新增员工规则
     * @param employeeNumberRuleVo
     * @param userSession
     * @return
     */
    void addEmployeeNumberRule(EmployeeNumberRuleVo employeeNumberRuleVo, UserSession userSession);
    /**
     * 新增合同参数表
     */
    void addContractParam(ContractParamVo contractParamVo, UserSession userSession);
    /**
     * 生成工号与合同编码
     * @param creatNumberVo
     * @param userSession
     * @return
     * @throws Exception
     */
    String createNumber(CreatNumberVo creatNumberVo, UserSession userSession) throws Exception;
}
