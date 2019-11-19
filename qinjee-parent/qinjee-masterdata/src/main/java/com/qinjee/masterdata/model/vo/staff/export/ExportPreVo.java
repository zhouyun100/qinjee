package com.qinjee.masterdata.model.vo.staff.export;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@JsonInclude
public class ExportPreVo implements Serializable {

    /** 用户姓名 */
    private String user_name;

    /** 电话 */
    private String phone;

    /** 性别 */
    private String gender;

    /** 邮箱 */
    private String email;

    /** 证件类型 */
    private String id_type;

    /** 证件号码 */
    private String id_number;

    /** 年龄 */
    private Integer age;

    /** 参加工作时间 */
    private Date first_work_date;

    /** 婚姻状况 */
    private String marital_status;

    /** 最高学历 */
    private String highest_degree;

    /** 毕业院校 */
    private String graduated_school;

    /** 毕业专业 */
    private String graduated_speciality;

    /** 最近工作单位 */
    private String last_work_company;

    /** 是否已育 */
    private Integer is_give_birth;

    /** 户口性质 */
    private String resident_character;

    /** 身高 */
    private BigDecimal height;

    /** 血型 */
    private String blood_type;

    /** 英文名 */
    private String english_name;

    /** 民族 */
    private Integer nationality;

    /** 出生日期 */
    private Date birth_date;

    /** 政治面貌 */
    private Integer political_status;

    /** 藉贯 */
    private String birthplace;

    /** 入职岗位 */
    private String application_position;

    /** 试用期限(月) */
    private Integer probation_period;

    /** 入职日期 */
    private Date hire_date;

    /** 入职部门 */
    private Integer org_id;

    /** 入职岗位 */
    private Integer post_id;

    /** 入职状态 */
    private String employment_state;

    /** 入职登记 */
    private String employment_register;

    /** 备注 */
    private String description;

    /** 数据来源 */
    private String data_source;

    /** 企业ID */
    private Integer company_id;


}
