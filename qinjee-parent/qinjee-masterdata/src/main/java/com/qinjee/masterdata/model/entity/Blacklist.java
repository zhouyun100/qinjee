package com.qinjee.masterdata.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * t_blacklist
 * @author 
 */
@Data
@JsonInclude

public class Blacklist implements Serializable {
    /**
     * 黑名单ID
     */
    private Integer blacklistId;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 证件类型
     */
    private String idType;

    /**
     * 证件号码
     */
    private String idNumber;

    /**
     * 所属单位ID
     */
    private Integer businessUnitId;
    /**
     * 所属单位名称
     */
    private String businessUnitName;
    /**
     * 部门ID
     */
    private Integer orgId;
    /**
     * 部门姓名
     */
    private String orgName;

    /**
     * 岗位ID
     */
    private Integer postId;
    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 拉黑原因
     */
    private String blockReason;

    /**
     * 拉黑时间
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date blockTime;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 企业ID
     */
    private Integer companyId;

    /**
     * 操作人ID
     */
    private Integer operatorId;

    /**
     * 修改时间
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date updateTime;

    /**
     * 是否删除
     */
    private Short isDelete;

    private static final long serialVersionUID = 1L;
}