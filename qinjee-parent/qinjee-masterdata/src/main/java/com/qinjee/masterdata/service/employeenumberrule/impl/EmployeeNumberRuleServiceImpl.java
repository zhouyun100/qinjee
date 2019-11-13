package com.qinjee.masterdata.service.employeenumberrule.impl;

import com.qinjee.masterdata.dao.EmployeeNumberRuleDao;
import com.qinjee.masterdata.dao.staffdao.contractdao.ContractParamDao;
import com.qinjee.masterdata.model.entity.ContractParam;
import com.qinjee.masterdata.model.entity.EmployeeNumberRule;
import com.qinjee.masterdata.model.vo.staff.ContractParamVo;
import com.qinjee.masterdata.model.vo.staff.CreatNumberVo;
import com.qinjee.masterdata.model.vo.staff.EmployeeNumberRuleVo;
import com.qinjee.masterdata.service.employeenumberrule.IEmployeeNumberRuleService;
import com.qinjee.model.request.UserSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Administrator
 */
public class EmployeeNumberRuleServiceImpl implements IEmployeeNumberRuleService {
    @Autowired
    private EmployeeNumberRuleDao employeeNumberRuleDao;
    @Autowired
    private ContractParamDao contractParamDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addEmployeeNumberRule(EmployeeNumberRuleVo employeeNumberRuleVo, UserSession userSession) {
        EmployeeNumberRule employeeNumberRule = new EmployeeNumberRule();
        BeanUtils.copyProperties(employeeNumberRuleVo, employeeNumberRule);
        employeeNumberRule.setOperatorId(userSession.getArchiveId());
        employeeNumberRule.setCompanyId(userSession.getCompanyId());
        employeeNumberRule.setIsDelete((short) 0);
        employeeNumberRuleDao.insertSelective(employeeNumberRule);
    }

    @Override
    public void addContractParam(ContractParamVo contractParamVo, UserSession userSession) {
        ContractParam contractParam=new ContractParam();
        contractParam.setCompanyId(userSession.getCompanyId());
        contractParam.setIsDelete((short) 0);
        contractParam.setOperatorId(userSession.getArchiveId());
        contractParamDao.insertSelective(contractParam);
    }

    @Override
    public String createNumber(CreatNumberVo creatNumberVo, UserSession userSession) throws Exception {
        String employeeNumberPrefix = creatNumberVo.getRulePrefix();
        String dateModel = getDateModel(creatNumberVo.getDateRule());
        String employeeNumberInfix = creatNumberVo.getRulePrefix();
        String employeeNumberSuffix = creatNumberVo.getRuleInfix();
        String digtaNumber = getDigtaNumber(creatNumberVo.getDigitCapacity(), userSession.getArchiveId());
        return employeeNumberPrefix + dateModel + employeeNumberInfix + employeeNumberSuffix + digtaNumber;
    }

    private String getDateModel(String rule) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if ("YY".equals(rule)) {
            return year.substring(2, 3);
        }
        if ("YYYY".equals(rule)) {
            return year;
        }
        if ("YYMM".equals(rule)) {
            if (Integer.parseInt(month) < 10) {
                month = "0" + month;
            }
            return year.substring(2, 3) + month;
        }
        if ("YYYYMM".equals(rule)) {
            if (Integer.parseInt(month) < 10) {
                month = "0" + month;
            }
            return year + month;
        }
        if ("YYYYMMDD".equals(rule)) {
            if (Integer.parseInt(month) < 10) {
                month = "0" + month;
            }
            if (Integer.parseInt(day) < 10) {
                month = "0" + month;
            }
            return year + month + day;
        }
        return null;
    }

    private String getDigtaNumber(short capacity, Integer number) throws Exception {
        String t = "";
        String s = String.valueOf(number);
        if (s.length() > capacity) {
            throw new Exception("sorry,你选的位数不够");
        }
        if (s.length() == capacity) {
            return String.valueOf(number);
        }
        if (s.length() < capacity) {
            int i = capacity - s.length();
            for (int j = 0; j < i; j++) {
                t += "0";
            }
            return t + number;
        }
        return null;
    }
}
