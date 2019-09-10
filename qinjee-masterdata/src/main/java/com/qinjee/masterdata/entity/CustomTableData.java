package com.qinjee.masterdata.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_custom_table_data
 * @author 
 */
public class CustomTableData implements Serializable {
    private Integer id;

    private Integer tableId;

    private Integer businessId;

    private String bigData;

    private Date createTime;

    private Integer creatorId;

    private Date operatorTime;

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

    public String getBigData() {
        return bigData;
    }

    public void setBigData(String bigData) {
        this.bigData = bigData;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Date getOperatorTime() {
        return operatorTime;
    }

    public void setOperatorTime(Date operatorTime) {
        this.operatorTime = operatorTime;
    }
}