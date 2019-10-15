package com.qinjee.masterdata.service.staff.impl;

import com.qinjee.masterdata.dao.EmployeeNumberRuleDao;
import com.qinjee.masterdata.model.entity.EmployeeNumberRule;
import com.qinjee.masterdata.model.vo.staff.EmployeeNumberRuleVo;
import com.qinjee.masterdata.service.staff.EmployeeNumberRuleService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月25日 09:47:00
 */
@Service
public class EmployeeNumberRuleServiceImpl implements EmployeeNumberRuleService {

    @Autowired
    private EmployeeNumberRuleDao employeeNumberRuleDao;

    @Transactional
    @Override
    public ResponseResult addEmployeeNumberRule(EmployeeNumberRuleVo employeeNumberRuleVo, UserSession userSession) {
        EmployeeNumberRule employeeNumberRule = new EmployeeNumberRule();
        BeanUtils.copyProperties(employeeNumberRuleVo, employeeNumberRule);
        employeeNumberRule.setOperatorId(userSession.getArchiveId());
        employeeNumberRule.setCompanyId(userSession.getCompanyId());
        employeeNumberRule.setIsDelete((short) 0);
        employeeNumberRuleDao.insertSelective(employeeNumberRule);
        return new ResponseResult();
    }
}
