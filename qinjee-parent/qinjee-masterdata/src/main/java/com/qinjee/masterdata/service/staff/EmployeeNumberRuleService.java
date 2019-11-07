package com.qinjee.masterdata.service.staff;

import com.qinjee.masterdata.model.vo.staff.EmployeeNumberRuleVo;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.ResponseResult;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月25日 09:47:00
 */
public interface EmployeeNumberRuleService {
    /**
     * 新增员工规则
     * @param employeeNumberRuleVo
     * @param userSession
     * @return
     */
    ResponseResult addEmployeeNumberRule(EmployeeNumberRuleVo employeeNumberRuleVo, UserSession userSession);

    ResponseResult createEmployeeNumber(Integer id,UserSession userSession) throws Exception;
}
