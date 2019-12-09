package com.qinjee.masterdata.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 工号规则表
 * @author
 */
@Data
@NoArgsConstructor
@ApiModel(description = "工号规则表实体类")
public class EmployeeNumberRule implements Serializable {
    /**
     * 工号规则ID
     */
    @ApiModelProperty("工号规则ID")
    private Integer enRuleId;

    /**
     * 工号前缀
     */
    @ApiModelProperty("工号前缀")
    private String employeeNumberPrefix;

    /**
     * 日期规则
     */
    @ApiModelProperty("日期规则")
    private String dateRule;

    /**
     * 工号中缀
     */
    @ApiModelProperty("工号中缀")
    private String employeeNumberInfix;

    /**
     * 流水号位数
     */
    @ApiModelProperty("流水号位数")
    private Short digitCapacity;

    /**
     * 工号后缀
     */
    @ApiModelProperty("工号后缀")
    private String employeeNumberSuffix;

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Integer companyId;

    /**
     * 操作人ID
     */
    @ApiModelProperty("操作人ID")
    private Integer operatorId;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd" )//页面写入数据库时格式化
    @JSONField(format = "yyyy-MM-dd ")//数据库导出页面时json格式化
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd" )//页面写入数据库时格式化
    @JSONField(format = "yyyy-MM-dd ")//数据库导出页面时json格式化
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 是否删除
     */
    @ApiModelProperty("是否删除")
    private Short isDelete;

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

    public String getEmployeeNumberInfix() {
        return employeeNumberInfix;
    }

    public void setEmployeeNumberInfix(String employeeNumberInfix) {
        this.employeeNumberInfix = employeeNumberInfix;
    }

    public Short getDigitCapacity() {
        return digitCapacity;
    }

    public void setDigitCapacity(Short digitCapacity) {
        this.digitCapacity = digitCapacity;
    }

    public String getEmployeeNumberSuffix() {
        return employeeNumberSuffix;
    }

    public void setEmployeeNumberSuffix(String employeeNumberSuffix) {
        this.employeeNumberSuffix = employeeNumberSuffix;
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    private static final long serialVersionUID = 1L;

}
