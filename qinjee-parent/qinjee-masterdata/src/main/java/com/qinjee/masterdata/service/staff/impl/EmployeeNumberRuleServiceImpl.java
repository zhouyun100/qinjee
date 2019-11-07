package com.qinjee.masterdata.service.staff.impl;

import com.qinjee.masterdata.dao.EmployeeNumberRuleDao;
import com.qinjee.masterdata.model.entity.EmployeeNumberRule;
import com.qinjee.masterdata.model.vo.staff.EmployeeNumberRuleVo;
import com.qinjee.masterdata.service.staff.EmployeeNumberRuleService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

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

    @Override
    public ResponseResult createEmployeeNumber(Integer id,UserSession userSession) throws Exception {
        EmployeeNumberRule employeeNumberRule = employeeNumberRuleDao.selectByPrimaryKey(id);
        if(employeeNumberRule!=null){
            throw new Exception("没有你要找的工号生成规则吗？");
        }
        String employeeNumberPrefix = employeeNumberRule.getEmployeeNumberPrefix();
        String dateModel = getDateModel(employeeNumberRule.getDateRule());
        String employeeNumberInfix = employeeNumberRule.getEmployeeNumberInfix();
        String employeeNumberSuffix = employeeNumberRule.getEmployeeNumberSuffix();
        String digtaNumber = getDigtaNumber(employeeNumberRule.getDigitCapacity(), userSession.getArchiveId());
        String s = employeeNumberPrefix + dateModel + employeeNumberInfix + employeeNumberSuffix + digtaNumber;
        return new ResponseResult(s, CommonCode.SUCCESS);
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

    private String getDigtaNumber(short capacity, Integer id) throws Exception {
        String t = "";
        String s = String.valueOf(id);
        if (s.length() > capacity) {
            throw new Exception("sorry,你选的位数不够");

        }
        if (s.length() == capacity) {
            return String.valueOf(id);
        }
        if (s.length() < capacity) {
            int i = capacity - s.length();
            for (int j = 0; j < i; j++) {
                t += "0";
            }
            return t + id;
        }
        return null;
    }


}
