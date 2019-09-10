package com.qinjee.masterdata.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * t_user_archive
 * @author
 */
public class UserArchive implements Serializable {
    /**
     * 档案ID
     */
    private Integer archiveId;

    /**
     * 人员ID
     */
    private Integer userId;

    /**
     * 工号
     */
    private String employeeNumber;

    /**
     * 企业ID
     */
    private Integer companyId;

    /**
     * 人员分类
     */
    private String userCategory;

    /**
     * 单位ID
     */
    private Integer businessUnitId;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 证件类型
     */
    private String idType;

    /**
     * 证件号码
     */
    private String idNumber;

    /**
     * 性别
     */
    private String gender;

    /**
     * 出生日期
     */
    private Date birthdate;

    /**
     * 年龄
     */
    private Short age;

    /**
     * 民族
     */
    private Integer nationality;

    /**
     * 籍贯
     */
    private String birthplace;

    /**
     * 政治面貌
     */
    private Integer politicalStatus;

    /**
     * 婚姻状况
     */
    private String maritalStatus;

    /**
     * 参加工作时间
     */
    private Date firstWorkDate;

    /**
     * 入职时间
     */
    private Date hiredate;

    /**
     * 司龄
     */
    private BigDecimal servingAge;

    /**
     * 工龄
     */
    private BigDecimal workingPeriod;

    /**
     * 试用到期时间
     */
    private Date probationDueDate;

    /**
     * 试用期限(月)
     */
    private Integer probationPeriod;

    /**
     * 转正时间
     */
    private Date converseDate;

    /**
     * 第一学历
     */
    private String firstDegree;

    /**
     * 最高学历
     */
    private String highestDegree;

    /**
     * 联系电话
     */
    private String tel;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 现住址
     */
    private String address;

    /**
     * 职业资格
     */
    private String professionalCertification;

    /**
     * 职称
     */
    private String professionalTitle;

    /**
     * 职称等级
     */
    private String professionalLevel;

    /**
     * 减员时间
     */
    private Date attritionDate;

    /**
     * 减员类型
     */
    private String attritionType;

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

    public Integer getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(Integer archiveId) {
        this.archiveId = archiveId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getUserCategory() {
        return userCategory;
    }

    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
    }

    public Integer getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(Integer businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Short getAge() {
        return age;
    }

    public void setAge(Short age) {
        this.age = age;
    }

    public Integer getNationality() {
        return nationality;
    }

    public void setNationality(Integer nationality) {
        this.nationality = nationality;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public Integer getPoliticalStatus() {
        return politicalStatus;
    }

    public void setPoliticalStatus(Integer politicalStatus) {
        this.politicalStatus = politicalStatus;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Date getFirstWorkDate() {
        return firstWorkDate;
    }

    public void setFirstWorkDate(Date firstWorkDate) {
        this.firstWorkDate = firstWorkDate;
    }

    public Date getHiredate() {
        return hiredate;
    }

    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
    }

    public BigDecimal getServingAge() {
        return servingAge;
    }

    public void setServingAge(BigDecimal servingAge) {
        this.servingAge = servingAge;
    }

    public BigDecimal getWorkingPeriod() {
        return workingPeriod;
    }

    public void setWorkingPeriod(BigDecimal workingPeriod) {
        this.workingPeriod = workingPeriod;
    }

    public Date getProbationDueDate() {
        return probationDueDate;
    }

    public void setProbationDueDate(Date probationDueDate) {
        this.probationDueDate = probationDueDate;
    }

    public Integer getProbationPeriod() {
        return probationPeriod;
    }

    public void setProbationPeriod(Integer probationPeriod) {
        this.probationPeriod = probationPeriod;
    }

    public Date getConverseDate() {
        return converseDate;
    }

    public void setConverseDate(Date converseDate) {
        this.converseDate = converseDate;
    }

    public String getFirstDegree() {
        return firstDegree;
    }

    public void setFirstDegree(String firstDegree) {
        this.firstDegree = firstDegree;
    }

    public String getHighestDegree() {
        return highestDegree;
    }

    public void setHighestDegree(String highestDegree) {
        this.highestDegree = highestDegree;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfessionalCertification() {
        return professionalCertification;
    }

    public void setProfessionalCertification(String professionalCertification) {
        this.professionalCertification = professionalCertification;
    }

    public String getProfessionalTitle() {
        return professionalTitle;
    }

    public void setProfessionalTitle(String professionalTitle) {
        this.professionalTitle = professionalTitle;
    }

    public String getProfessionalLevel() {
        return professionalLevel;
    }

    public void setProfessionalLevel(String professionalLevel) {
        this.professionalLevel = professionalLevel;
    }

    public Date getAttritionDate() {
        return attritionDate;
    }

    public void setAttritionDate(Date attritionDate) {
        this.attritionDate = attritionDate;
    }

    public String getAttritionType() {
        return attritionType;
    }

    public void setAttritionType(String attritionType) {
        this.attritionType = attritionType;
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
