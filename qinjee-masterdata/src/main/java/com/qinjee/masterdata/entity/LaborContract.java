package com.qinjee.masterdata.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_labor_contract
 * @author 
 */
public class LaborContract implements Serializable {
    /**
     * 合同ID
     */
    private Integer contractId;

    /**
     * 档案ID
     */
    private Integer archiveId;

    /**
     * 证件类型
     */
    private String idType;

    /**
     * 证件号码
     */
    private String idNumber;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 工号
     */
    private String employeeNumber;

    /**
     * 性别
     */
    private String gender;

    /**
     * 单位ID
     */
    private Integer businessUnitId;

    /**
     * 机构ID
     */
    private Integer orgId;

    /**
     * 岗位ID
     */
    private Integer postId;

    /**
     * 试用开始日期
     */
    private Date probationBeginDate;

    /**
     * 试用结束日期
     */
    private Date probationEndDate;

    /**
     * 合同签订日期
     */
    private Date contractSignDate;

    /**
     * 合同开始日期
     */
    private Date contractBeginDate;

    /**
     * 合同结束日期
     */
    private Date contractEndDate;

    /**
     * 合同期限(月)
     */
    private Integer contractPeriodMonth;

    /**
     * 合同期限类型
     */
    private String contractPeriodType;

    /**
     * 合同主体
     */
    private String contractSubject;

    /**
     * 合同编号
     */
    private String contractNumber;

    /**
     * 签订次数
     */
    private Integer signNumber;

    /**
     * 操作人ID
     */
    private Integer operatorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Integer getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(Integer archiveId) {
        this.archiveId = archiveId;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(Integer businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Date getProbationBeginDate() {
        return probationBeginDate;
    }

    public void setProbationBeginDate(Date probationBeginDate) {
        this.probationBeginDate = probationBeginDate;
    }

    public Date getProbationEndDate() {
        return probationEndDate;
    }

    public void setProbationEndDate(Date probationEndDate) {
        this.probationEndDate = probationEndDate;
    }

    public Date getContractSignDate() {
        return contractSignDate;
    }

    public void setContractSignDate(Date contractSignDate) {
        this.contractSignDate = contractSignDate;
    }

    public Date getContractBeginDate() {
        return contractBeginDate;
    }

    public void setContractBeginDate(Date contractBeginDate) {
        this.contractBeginDate = contractBeginDate;
    }

    public Date getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Date contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public Integer getContractPeriodMonth() {
        return contractPeriodMonth;
    }

    public void setContractPeriodMonth(Integer contractPeriodMonth) {
        this.contractPeriodMonth = contractPeriodMonth;
    }

    public String getContractPeriodType() {
        return contractPeriodType;
    }

    public void setContractPeriodType(String contractPeriodType) {
        this.contractPeriodType = contractPeriodType;
    }

    public String getContractSubject() {
        return contractSubject;
    }

    public void setContractSubject(String contractSubject) {
        this.contractSubject = contractSubject;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public Integer getSignNumber() {
        return signNumber;
    }

    public void setSignNumber(Integer signNumber) {
        this.signNumber = signNumber;
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
}