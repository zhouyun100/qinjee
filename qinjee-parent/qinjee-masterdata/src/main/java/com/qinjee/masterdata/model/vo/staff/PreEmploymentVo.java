package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@JsonInclude
@JsonIgnoreProperties(ignoreUnknown = true)
public class PreEmploymentVo implements Serializable {
    /**
     * 版本号
     */
    private static final long serialVersionUID = 8110487415164769562L;

    /**
     * 预入职ID
     */
    private Integer employmentId;

    /**
     * 用户姓名
     */

    private String userName;

    /**
     * 电话
     */

    private String phone;

    /**
     * 性别
     */
    private String gender;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 证件类型
     */

    private String idType;
    /**
     * 证件类型名称
     */
    private String idTypeName;
    /**
     * 证件号码
     */

    private String idNumber;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 参加工作时间
     */

    private Date firstWorkDate;

    /**
     * 婚姻状况
     */
    private String maritalStatus;

    /**
     * 最高学历
     */
    private String highestDegree;

    /**
     * 毕业院校
     */
    private String graduatedSchool;

    /**
     * 毕业专业
     */
    private String graduatedSpeciality;

    /**
     * 最近工作单位
     */
    private String lastWorkCompany;

    /**
     * 是否已育
     */
    private Integer isGiveBirth;

    /**
     * 户口性质
     */
    private String residentCharacter;

    /**
     * 身高
     */
    private BigDecimal height;

    /**
     * 血型
     */
    private String bloodType;

    /**
     * 英文名
     */
    private String englishName;

    /**
     * 民族
     */
    private String nationality;

    /**
     * 出生日期
     */

    private Date birthDate;

    /**
     * 政治面貌
     */
    private String politicalStatus;

    /**
     * 藉贯
     */
    private String birthplace;

    /**
     * 应聘岗位
     */
    private String applicationPosition;

    /**
     * 试用期限(月)
     */
    private Integer probationPeriod;

    /**
     * 入职日期
     */

    private Date hireDate;

    /**
     * 入职部门
     */
    private Integer orgId;

    /**
     * 入职岗位
     */
    private Integer postId;

    /**
     * 入职状态
     */
    private String employmentState;

    /**
     * 入职登记
     */
    private String employmentRegister;

    /**
     * 备注
     */
    private String description;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 企业ID
     */
    private Integer companyId;


    /**
     * 岗位名称
     */
    private String postName;
    /**
     * 职位名称
     */
    private String orgName;
    /**
     * 学历名称
     */
    private String highestDegreeName;
    /**
     * 婚姻状况名称
     */
    private String marryStatusName;
    /**
     * 延期入职原因
     */
    private String delayReson;
    /**
     * 拉黑原因
     */
    private String blockReson;

    /**
     * 放弃原因
     */
    private String abandonReason;

    /**
     * 延期入职时间
     */

    private Date delayDate;
    /**
     * 部门编码
     */
    private String orgCode;
    /**
     * 岗位编码
     */
    private String postCode;
    /**
     * 职级id
     */
    private Integer positionLevelId;
    /**
     * 职等id
     */
    private Integer positionGradeId;
    /**
     * 职级名称
     */
    private Integer positionLevelName;
    /**
     * 职等名称
     */
    private Integer positionGradeName;
    /**
     * 模板id
     */
    private Integer templateId;
}
