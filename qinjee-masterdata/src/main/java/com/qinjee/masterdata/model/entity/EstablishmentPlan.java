package com.qinjee.masterdata.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_establishment_plan
 * @author
 */
public class EstablishmentPlan implements Serializable {
    /**
     * 编制计划ID
     */
    private Integer epId;

    /**
     * 编制计划名称
     */
    private String epName;

    /**
     * 人员类别
     */
    private String userType;

    /**
     * 人员状态(是否兼职)
     */
    private String userStatus;

    /**
     * 编制控制
     */
    private String epLimit;

    /**
     * 编制对象
     */
    private String epTarget;

    /**
     * 开始月
     */
    private Short beginMonth;

    /**
     * 结束月
     */
    private Short endMonth;

    /**
     * 流动人数
     */
    private Integer moveNumber;

    /**
     * 编制计划描述
     */
    private String epRemart;

    /**
     * 编制计划状态
     */
    private Short epStatus;

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
     * 是否启用
     */
    private Short isEnable;

    /**
     * 是否删除
     */
    private Short isDelete;

    private static final long serialVersionUID = 1L;

    public Integer getEpId() {
        return epId;
    }

    public void setEpId(Integer epId) {
        this.epId = epId;
    }

    public String getEpName() {
        return epName;
    }

    public void setEpName(String epName) {
        this.epName = epName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getEpLimit() {
        return epLimit;
    }

    public void setEpLimit(String epLimit) {
        this.epLimit = epLimit;
    }

    public String getEpTarget() {
        return epTarget;
    }

    public void setEpTarget(String epTarget) {
        this.epTarget = epTarget;
    }

    public Short getBeginMonth() {
        return beginMonth;
    }

    public void setBeginMonth(Short beginMonth) {
        this.beginMonth = beginMonth;
    }

    public Short getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(Short endMonth) {
        this.endMonth = endMonth;
    }

    public Integer getMoveNumber() {
        return moveNumber;
    }

    public void setMoveNumber(Integer moveNumber) {
        this.moveNumber = moveNumber;
    }

    public String getEpRemart() {
        return epRemart;
    }

    public void setEpRemart(String epRemart) {
        this.epRemart = epRemart;
    }

    public Short getEpStatus() {
        return epStatus;
    }

    public void setEpStatus(Short epStatus) {
        this.epStatus = epStatus;
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

    public Short getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Short isEnable) {
        this.isEnable = isEnable;
    }

    public Short getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Short isDelete) {
        this.isDelete = isDelete;
    }
}
