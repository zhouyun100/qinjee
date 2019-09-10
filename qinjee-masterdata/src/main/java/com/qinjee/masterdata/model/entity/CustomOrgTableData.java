package com.qinjee.masterdata.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 自定义表数据
 * @author
 */
public class CustomOrgTableData implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 表ID
     */
    private Integer tableId;

    /**
     * 业务ID
     */
    private Integer businessId;

    /**
     * 操作人ID
     */
    private Integer operatorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 操作时间
     */
    private Date updateTime;

    /**
     * 数据大字段
     */
    private byte[] bigData;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
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

    public byte[] getBigData() {
        return bigData;
    }

    public void setBigData(byte[] bigData) {
        this.bigData = bigData;
    }
}
