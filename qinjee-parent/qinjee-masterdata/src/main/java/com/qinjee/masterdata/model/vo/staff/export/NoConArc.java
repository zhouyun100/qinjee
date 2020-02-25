package com.qinjee.masterdata.model.vo.staff.export;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
public class NoConArc implements Serializable {
    /**
     * 档案ID
     */
    @ApiModelProperty("档案ID")
    private Integer archive_id;

    /**
     * 姓名
     */
    @NotNull
    @ApiModelProperty("姓名")
    private String user_name;


    /**
     * 工号
     */
    @NotNull
    @ApiModelProperty("工号")
    private String employee_number;

    /**
     * 单位名称
     */
    @ApiModelProperty("单位名称")
    private String business_unit_name;


    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    private String org_name;

    /**
     * 岗位名称
     */
    @ApiModelProperty("岗位名称")
    private String post_name;

    /**
     * 入职日期
     */
    private Date hire_date;

    /**
     * 试用期限（月）
     */
    private Integer  probation_period;
    /**
     * 试用到期时间
     */
    @ApiModelProperty("试用到期时间")
    private Date probation_due_date;

    /**
     * 人员分类
     */
    private String user_category;
}
