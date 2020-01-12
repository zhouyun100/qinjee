package com.qinjee.masterdata.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_pre_employment_change
 * @author
 */
@Data
@JsonInclude
public class PreEmploymentChange implements Serializable {
    /**
     * 变更ID
     */
    private Integer changeId;

    /**
     * 预入职ID
     */
    private Integer employmentId;

    /**
     * 变更状态
     */
    private String changeState;

    /**
     * 放弃原因
     */
    private String abandonReason;

    /**
     * 变更描述
     */
    private String changeRemark;

    /**
     * 操作人ID
     */
    private Integer operatorId;
    /**
     * 延期入职时间
     */
    private Date delayDate;
    /**
     * 创建时间
     */
    private Date createTime;


    private static final long serialVersionUID = 1L;

}
