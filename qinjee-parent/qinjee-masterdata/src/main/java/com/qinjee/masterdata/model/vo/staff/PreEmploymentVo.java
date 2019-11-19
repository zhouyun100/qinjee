package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
@JsonInclude
public class PreEmploymentVo implements Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 1L;

    /** 预入职ID */
    private Integer employmentId;

    /** 用户姓名 */
    @NotNull
    private String userName;

    /** 电话 */
    @NotNull
    private String phone;

    /** 性别 */
    @NotNull
    private String gender;

    /** 邮箱 */
    @NotNull
    private String email;

    /** 证件类型 */
    private String idType;

    /** 证件号码 */
    private String idNumber;

    /** 年龄 */
    private Integer age;

    /** 参加工作时间 */
    private Date firstWorkDate;

    /** 婚姻状况 */
    @NotNull
    private String maritalStatus;

    /** 最高学历 */
    @NotNull
    private String highestDegree;

    /** 毕业院校 */
    private String graduatedSchool;

    /** 毕业专业 */
    @NotNull
    private String graduatedSpeciality;

    /** 最近工作单位 */
    private String lastWorkCompany;

    /** 是否已育 */
    private Integer isGiveBirth;

    /** 户口性质 */
    private String residentCharacter;

    /** 身高 */
    private BigDecimal height;

    /** 血型 */
    private String bloodType;

    /** 英文名 */
    private String englishName;

    /** 民族 */
    private Integer nationality;

    /** 出生日期 */
    private Date birthDate;

    /** 政治面貌 */
    private Integer politicalStatus;

    /** 藉贯 */
    private String birthplace;

    /** 入职岗位 */
    @NotNull
    private String applicationPosition;

    /** 试用期限(月) */
    private Integer probationPeriod;

    /** 入职日期 */
    private Date hireDate;

    /** 入职部门 */
    private Integer orgId;

    /** 入职岗位 */
    private Integer postId;

    /** 入职状态 */
    private String employmentState;

    /** 入职登记 */
    private String employmentRegister;

    /** 备注 */
    private String description;

    /** 数据来源 */
    private String dataSource;


}
