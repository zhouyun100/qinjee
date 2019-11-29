package com.qinjee.masterdata.model.vo.staff.export;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qinjee.utils.QueryColumn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
@JsonInclude
public class ImportArcVo implements Serializable {
    /**
     * 姓名
     */
    @NotNull
    @QueryColumn("tua.user_name")
    @ApiModelProperty("姓名")
    private String user_name;
    /**
     * 性别
     */
    @NotNull
    @QueryColumn("tua.gender")
    @ApiModelProperty("性别")
    private String gender;

    /**
     * 证件类型
     */
    @NotNull
    @ApiModelProperty("证件类型")
    private String id_type;

    /**
     * 证件号码
     */
    @NotNull
    @ApiModelProperty("证件号码")
    private String id_number;
    /**
     * 出生日期
     */
    @NotNull
    @ApiModelProperty("出生日期")
    private Date birth_date;
    /**
     * 年龄
     */
    @NotNull
    @ApiModelProperty("年龄")
    private Short age;
    /**
     * 籍贯
     */
    @NotNull
    @ApiModelProperty("籍贯")
    private String birthplace;

    /**
     * 民族
     */
    @NotNull
    @ApiModelProperty("民族")
    private Integer nationality;

    /**
     * 最高学历
     */
    @ApiModelProperty("最高学历")
    private String highest_degree;

    /**
     * 第一学历
     */
    @NotNull
    @ApiModelProperty("第一学历")
    private String first_degree;

    /**
     * 联系电话
     */
    @NotNull
    @QueryColumn("tua.tel")
    @ApiModelProperty("联系电话")
    private String phone;


    /**
     * 电子邮箱
     */
    @NotNull
    @QueryColumn("tua.email")
    @ApiModelProperty("电子邮箱")
    private String email;

    /**
     * 婚姻状况
     */
    @ApiModelProperty("婚姻状况")
    private String marital_status;

    /**
     * 政治面貌
     */
    @ApiModelProperty("政治面貌")
    private Integer political_status;

    /**
     * 现住址
     */
    @NotNull
    @ApiModelProperty("现住址")
    private String address;

    /**
     * 职业资格
     */
    @ApiModelProperty("职业资格")
    private String professional_certification;

    /**
     * 职称
     */
    @ApiModelProperty("职称")
    private String professional_title;


    /**
     * 职称等级
     */
    @ApiModelProperty("职称等级")
    private String professional_level;


    /**
     * 工号
     */
    @NotNull
    @ApiModelProperty("工号")
    private String employee_number;

    /**
     * 入职时间
     */
    @QueryColumn("tua.hiredate")
    @ApiModelProperty("入职时间")
    private Date hire_date;

    /**
     * 人员分类   多级代码：在职（正式、试用、实习）、不在职（离职、退休）
     */
    @ApiModelProperty("人员分类")
    private String user_category;

    /**
     * 参加工作时间
     */
    @ApiModelProperty("参加工作时间")
    private Date first_work_date;
    /**
     * 司龄
     */
    @ApiModelProperty("司龄")
    private BigDecimal serving_age;

    /**
     * 工龄
     */
    @ApiModelProperty("工龄")
    private BigDecimal working_period;
    /**
     * 试用到期时间
     */
    @ApiModelProperty("试用到期时间")
    private Date probation_due_date;



    /**
     * 试用期限
     */
    @ApiModelProperty("试用期限(月)")
    private Integer probation_period;


    /**
     * 部门ID
     */
    @QueryColumn("tua.org_id")
    @ApiModelProperty("部门ID")
    private Integer org_id;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    private Integer orgName;
    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    private String org_code;

    /**
     * 岗位ID
     */
    @QueryColumn("tua.post_id")
    @ApiModelProperty("岗位ID")
    private Integer postId;

    /**
     * 岗位名称
     */
    @ApiModelProperty("岗位名称")
    private Integer postName;

    /**
     * 岗位编码
     */
    @ApiModelProperty("岗位编码")
    private String post_code;

    /**
     * 转正时间
     */
    @ApiModelProperty("转正时间")
    private Date converseDate;


    /**
     * 上级领导Id
     */
    @ApiModelProperty("上级领导Id")
    @QueryColumn("tua.supervisor_id")
    private Integer supervisor_id;

    /**
     * 上级领导姓名
     */
    @ApiModelProperty("上级领导姓名")
    private Integer supervisor_user_name;

    /**
     * 上级领导工号
     */
    @ApiModelProperty("上级领导姓名")
    private String supervisor_code;

























}
