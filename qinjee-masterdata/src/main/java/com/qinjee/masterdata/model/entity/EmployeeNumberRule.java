package com.qinjee.masterdata.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_employee_number_rule
 * @author
 */
public class EmployeeNumberRule implements Serializable {
    /**
     * 工号规则ID
     */
    private Integer enRuleId;

    /**
     * 工号前缀
     */
    private String employeeNumberPrefix;

    /**
     * 日期规则
     */
    private String dateRule;

    /**
     * 流水号位数
     */
    private Short digitCapacity;

    /**
     * 企业ID
     */
    private Integer companyId;

    /**
     * 操作人ID
     */
    private Integer operatorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Short isDelete;

    private static final long serialVersionUID = 1L;

    public Integer getEnRuleId() {
        return enRuleId;
    }

    public void setEnRuleId(Integer enRuleId) {
        this.enRuleId = enRuleId;
    }

    public String getEmployeeNumberPrefix() {
        return employeeNumberPrefix;
    }

    public void setEmployeeNumberPrefix(String employeeNumberPrefix) {
        this.employeeNumberPrefix = employeeNumberPrefix;
    }

    public String getDateRule() {
        return dateRule;
    }

    public void setDateRule(String dateRule) {
        this.dateRule = dateRule;
    }

    public Short getDigitCapacity() {
        return digitCapacity;
    }

    public void setDigitCapacity(Short digitCapacity) {
        this.digitCapacity = digitCapacity;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Short getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Short isDelete) {
        this.isDelete = isDelete;
    }
}
