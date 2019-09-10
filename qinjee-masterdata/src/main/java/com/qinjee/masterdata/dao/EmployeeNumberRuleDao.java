package com.qinjee.masterdata.dao;

import com.qinjee.masterdata.entity.EmployeeNumberRule;

public interface EmployeeNumberRuleDao {
    int deleteByPrimaryKey(Integer enRuleId);

    int insert(EmployeeNumberRule record);

    int insertSelective(EmployeeNumberRule record);

    EmployeeNumberRule selectByPrimaryKey(Integer enRuleId);

    int updateByPrimaryKeySelective(EmployeeNumberRule record);

    int updateByPrimaryKey(EmployeeNumberRule record);
}