package com.qinjee.masterdata.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 员工档案表
 * @author
 */
@ApiModel(description = "员工档案表")
@Data
@NoArgsConstructor
public class UserArchive implements Serializable {
    /**
     * 档案ID
     */
    @ApiModelProperty("档案ID")
    private Integer archiveId;

    /**
     * 人员ID
     */
    @ApiModelProperty("人员ID")
    private Integer userId;

    /**
     * 工号
     */
    @ApiModelProperty("工号")
    private String employeeNumber;

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Integer companyId;

    /**
     * 人员分类
     */
    @ApiModelProperty("人员分类")
    private String userCategory;

    /**
     * 单位ID
     */
    @ApiModelProperty("单位ID")
    private Integer businessUnitId;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String userName;

    /**
     * 证件类型
     */
    @ApiModelProperty("证件类型")
    private String idType;

    /**
     * 证件号码
     */
    @ApiModelProperty("证件号码")
    private String idNumber;

    /**
     * 性别
     */
    @ApiModelProperty("性别")
    private String gender;

    /**
     * 出生日期
     */
    @ApiModelProperty("出生日期")
    private Date birthdate;

    /**
     * 年龄
     */
    @ApiModelProperty("年龄")
    private Short age;

    /**
     * 民族
     */
    @ApiModelProperty("民族")
    private Integer nationality;

    /**
     * 籍贯
     */
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
    @ApiModelProperty("入职时间")
    private Date hiredate;

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
    @ApiModelProperty("联系电话")
    private String tel;

    /**
     * 电子邮箱
     */
    @ApiModelProperty("电子邮箱")
    private String email;

    /**
     * 现住址
     */
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
     * 操作人ID
     */
    @ApiModelProperty("操作人ID")
    private Integer operatorId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

}
