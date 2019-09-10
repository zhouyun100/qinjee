package com.qinjee.masterdata.entity;

import java.io.Serializable;

/**
 * t_ep_range
 * @author 
 */
public class EpRange implements Serializable {
    /**
     * 范围ID
     */
    private Integer rangeId;

    /**
     * 编制计划ID
     */
    private Integer epId;

    /**
     * 范围内型
     */
    private String rangeType;

    /**
     * 范围业务ID
     */
    private Integer rangeBusinessId;

    private static final long serialVersionUID = 1L;

    public Integer getRangeId() {
        return rangeId;
    }

    public void setRangeId(Integer rangeId) {
        this.rangeId = rangeId;
    }

    public Integer getEpId() {
        return epId;
    }

    public void setEpId(Integer epId) {
        this.epId = epId;
    }

    public String getRangeType() {
        return rangeType;
    }

    public void setRangeType(String rangeType) {
        this.rangeType = rangeType;
    }

    public Integer getRangeBusinessId() {
        return rangeBusinessId;
    }

    public void setRangeBusinessId(Integer rangeBusinessId) {
        this.rangeBusinessId = rangeBusinessId;
    }
}