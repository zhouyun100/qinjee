package com.qinjee.masterdata.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd" )//页面写入数据库时格式化
    @JSONField(format = "yyyy-MM-dd ")//数据库导出页面时json格式化
    private Date delayDate;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd" )//页面写入数据库时格式化
    @JSONField(format = "yyyy-MM-dd ")//数据库导出页面时json格式化
    private Date createTime;


    private static final long serialVersionUID = 1L;

}
