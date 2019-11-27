package com.qinjee.masterdata.model.vo.staff;

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
public class UserArchiveVo implements Serializable {
    /**
     * 档案ID
     */
    @ApiModelProperty("档案ID")
    private Integer archiveId;

    /**
     * 人员ID
     */
    @NotNull
    @ApiModelProperty("人员ID")
    private Integer userId;

    /**
     * 工号
     */
    @NotNull
    @ApiModelProperty("工号")
    private String employeeNumber;

    /**
     * 企业ID
     */
    @NotNull
    @ApiModelProperty("企业ID")
    private Integer companyId;

    /**
     * 人员分类   多级代码：在职（正式、试用、实习）、不在职（离职、退休）
     */
    @ApiModelProperty("人员分类")
    private String userCategory;

    /**
     * 单位ID
     */
    @ApiModelProperty("单位ID")
    @QueryColumn("tua.business_unit_id")
    private Integer businessUnitId;

    /**
     * 单位名称
     */
    @ApiModelProperty("单位名称")
    private Integer businessUnitName;

    /**
     * 部门ID
     */
    @QueryColumn("tua.org_id")
    @ApiModelProperty("部门ID")
    private Integer orgId;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    private Integer orgName;

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
     * 姓名
     */
    @NotNull
    @QueryColumn("tua.user_name")
    @ApiModelProperty("姓名")
    private String userName;

    /**
     * 证件类型
     */
    @NotNull
    @ApiModelProperty("证件类型")
    private String idType;

    /**
     * 证件号码
     */
    @NotNull
    @ApiModelProperty("证件号码")
    private String idNumber;

    /**
     * 性别
     */
    @NotNull
    @QueryColumn("tua.gender")
    @ApiModelProperty("性别")
    private String gender;

    /**
     * 出生日期
     */
    @NotNull
    @ApiModelProperty("出生日期")
    private Date birthDate;

    /**
     * 年龄
     */
    @NotNull
    @ApiModelProperty("年龄")
    private Short age;

    /**
     * 民族
     */
    @NotNull
    @ApiModelProperty("民族")
    private Integer nationality;

    /**
     * 籍贯
     */
    @NotNull
    @ApiModelProperty("籍贯")
    private String birthplace;

    /**
     * 政治面貌
     */
    @ApiModelProperty("政治面貌")
    private Integer politicalStatus;

    /**
     * 婚姻状况
     */
    @ApiModelProperty("婚姻状况")
    private String maritalStatus;

    /**
     * 参加工作时间
     */
    @ApiModelProperty("参加工作时间")
    private Date firstWorkDate;

    /**
     * 入职时间
     */
    @QueryColumn("tua.hiredate")
    @ApiModelProperty("入职时间")
    private Date hireDate;

    /**
     * 司龄
     */
    @ApiModelProperty("司龄")
    private BigDecimal servingAge;

    /**
     * 工龄
     */
    @ApiModelProperty("工龄")
    private BigDecimal workingPeriod;

    /**
     * 试用到期时间
     */
    @ApiModelProperty("试用到期时间")
    private Date probationDueDate;

    /**
     * 试用期限(月)
     */
    @ApiModelProperty("试用期限(月)")
    private Integer probationPeriod;

    /**
     * 转正时间
     */
    @ApiModelProperty("转正时间")
    private Date converseDate;

    /**
     * 第一学历
     */
    @NotNull
    @ApiModelProperty("第一学历")
    private String firstDegree;

    /**
     * 最高学历
     */
    @ApiModelProperty("最高学历")
    private String highestDegree;

    /**
     * 联系电话
     */
    @NotNull
    @QueryColumn("tua.phone")
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
     * 现住址
     */
    @NotNull
    @ApiModelProperty("现住址")
    private String address;

    /**
     * 职业资格
     */
    @ApiModelProperty("职业资格")
    private String professionalCertification;

    /**
     * 职称
     */
    @ApiModelProperty("职称")
    private String professionalTitle;

    /**
     * 职称等级
     */
    @ApiModelProperty("职称等级")
    private String professionalLevel;

    /**
     * 减员时间
     */
    @ApiModelProperty("减员时间")
    private Date attritionDate;

    /**
     * 减员类型
     */
    @ApiModelProperty("减员类型")
    private String attritionType;

    /**
     * 上级领导Id
     */
    @ApiModelProperty("上级领导Id")
    @QueryColumn("tua.supervisor_id")
    private Integer supervisorId;

    /**
     * 上级领导姓名
     */
    @ApiModelProperty("上级领导姓名")
    private Integer supervisorUserName;

    private static final long serialVersionUID = 1L;

}
