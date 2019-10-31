package com.qinjee.masterdata.model.vo.staff;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
public class ExportPreVo implements Serializable {


    /**
     * 姓名
     */
    @NotNull
    private String userName;


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
    /**
     * 放弃原因
     */
    private String abandonReason;

    /**
     * 变更描述
     */
    private String changeRemark;
    /**
     * 拉黑原因
     */
    private String blockReason;
    private String email;
    private static final long serialVersionUID = 1L;
}
