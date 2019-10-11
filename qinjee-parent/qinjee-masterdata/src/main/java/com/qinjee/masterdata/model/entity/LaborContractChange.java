package com.qinjee.masterdata.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * t_labor_contract_change
 * @author
 */
public class LaborContractChange implements Serializable {
    /**
     * 变更ID
     */
    private Integer changeId;

    /**
     * 合同ID
     */
    private Integer contractId;

    /**
     * 变更类型
     */
    private String changeType;

    /**
     * 变更日期
     */
    private Date changeDate;

    /**
     * 变更原因
     */
    private String changeReason;

    /**
     * 补偿金额
     */
    private BigDecimal compensateAmount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作人ID
     */
    private Integer operatorId;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Integer getChangeId() {
        return changeId;
    }

    public void setChangeId(Integer changeId) {
        this.changeId = changeId;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public BigDecimal getCompensateAmount() {
        return compensateAmount;
    }

    public void setCompensateAmount(BigDecimal compensateAmount) {
        this.compensateAmount = compensateAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
}
