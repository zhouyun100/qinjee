package com.qinjee.masterdata.model.entity;

import java.io.Serializable;

/**
 * t_ep_container
 * @author
 */
public class EpContainer implements Serializable {
    /**
     * 实例ID
     */
    private Integer containerId;

    /**
     * 范围ID
     */
    private Integer rangeId;

    /**
     * 编制月份
     */
    private Integer cpMonth;

    /**
     * 编制数量
     */
    private Integer cpNumber;

    private static final long serialVersionUID = 1L;

    public Integer getContainerId() {
        return containerId;
    }

    public void setContainerId(Integer containerId) {
        this.containerId = containerId;
    }

    public Integer getRangeId() {
        return rangeId;
    }

    public void setRangeId(Integer rangeId) {
        this.rangeId = rangeId;
    }

    public Integer getCpMonth() {
        return cpMonth;
    }

    public void setCpMonth(Integer cpMonth) {
        this.cpMonth = cpMonth;
    }

    public Integer getCpNumber() {
        return cpNumber;
    }

    public void setCpNumber(Integer cpNumber) {
        this.cpNumber = cpNumber;
    }
}
