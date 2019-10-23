package com.qinjee.masterdata.model.entity;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * t_pre_employment
 * @author 
 */
@Data
@ToString
public class PreEmployment implements Serializable {
    /**
     * 入职ID
     */
    private Integer employmentId;

    /**
     * 姓名
     */
    @NotNull
    private String userName;

    /**
     * 工号
     */
    private String employeeNumber;

    /**
     * 企业ID
     */
    @NotNull
    private Integer companyId;

    /**
     * 应聘职位
     */
    @NotNull
    private String positionName;

    /**
     * 入职机构
     */
    @NotNull
    private Integer orgId;

    /**
     * 入职岗位
     */
    @NotNull
    private Integer postId;

    /**
     * 入职日期
     */
    private Date employmentDate;

    /**
     * 入职状态
     */
    @NotNull
    private String employmentState;

    /**
     * 入职登记
     */
    private String employmentRegister;

    /**
     * 手机
     */
    @NotNull
    private String phone;

    /**
     * 邮箱
     */
    @NotNull
    private String email;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Short isDelete;

    private static final long serialVersionUID = 1L;
}