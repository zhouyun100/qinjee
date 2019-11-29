package com.qinjee.masterdata.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 员工档案岗位关系表
 * @author
 */
@ApiModel(description = "员工档案岗位关系表")
@Data
@JsonInclude
public class UserArchivePostRelation implements Serializable {
    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    private Integer id;

    /**
     * 员工档案ID
     */
    @NotNull
    @ApiModelProperty("员工档案ID")
    private Integer archiveId;
    /**
     * 单位ID
     */
    @ApiModelProperty("单位ID")
    private Integer businessUnitId;
    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    private Integer orgId;

    /**
     * 岗位ID
     */
    @NotNull
    @ApiModelProperty("岗位ID")
    private Integer postId;

    /**
     * 职级ID
     */
    @ApiModelProperty("职级ID")
    private Integer positionLevelId;

    /**
     * 职等ID
     */
    @ApiModelProperty("职等ID")
    private Integer positionGradeId;

    /**
     * 任职开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//页面写入数据库时格式化
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")//数据库导出页面时json格式化
    @ApiModelProperty("任职开始时间")
    private Date employmentBeginDate;

    /**
     * 任职结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//页面写入数据库时格式化
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")//数据库导出页面时json格式化
    @ApiModelProperty("任职结束时间")
    private Date employmentEndDate;

    /**
     * 任职类型
     */
    @ApiModelProperty("任职类型")
    private String employmentType;

    /**
     * 任免原因
     */
    @ApiModelProperty("任免原因")
    private String appointDismissalReason;

    /**
     * 上级领导ID
     */
    @ApiModelProperty("上级领导ID")
    private Integer supervisorId;

    /**
     * 操作人ID
     */
    @ApiModelProperty("操作人ID")
    private Integer operatorId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 是否删除
     */
    @ApiModelProperty("是否删除")
    private Short isDelete;

    /**
     * 姓名
     */
    @NotNull
    @ApiModelProperty("姓名")
    private String userName;

    /**
     * 工号
     */
    @NotNull
    @ApiModelProperty("工号")
    private String employeeNumber;

    /**
     * 岗位名称
     */
    @ApiModelProperty("岗位名称")
    private String postName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(Integer archiveId) {
        this.archiveId = archiveId;
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

    public Integer getPositionLevelId() {
        return positionLevelId;
    }

    public void setPositionLevelId(Integer positionLevelId) {
        this.positionLevelId = positionLevelId;
    }

    public Integer getPositionGradeId() {
        return positionGradeId;
    }

    public void setPositionGradeId(Integer positionGradeId) {
        this.positionGradeId = positionGradeId;
    }

    public Date getEmploymentBeginDate() {
        return employmentBeginDate;
    }

    public void setEmploymentBeginDate(Date employmentBeginDate) {
        this.employmentBeginDate = employmentBeginDate;
    }

    public Date getEmploymentEndDate() {
        return employmentEndDate;
    }

    public void setEmploymentEndDate(Date employmentEndDate) {
        this.employmentEndDate = employmentEndDate;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getAppointDismissalReason() {
        return appointDismissalReason;
    }

    public void setAppointDismissalReason(String appointDismissalReason) {
        this.appointDismissalReason = appointDismissalReason;
    }

    public Integer getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(Integer supervisorId) {
        this.supervisorId = supervisorId;
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

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    private static final long serialVersionUID = 1L;

}
