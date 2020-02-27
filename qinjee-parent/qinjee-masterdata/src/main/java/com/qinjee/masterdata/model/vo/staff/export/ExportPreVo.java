package com.qinjee.masterdata.model.vo.staff.export;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Administrator
 */
@Data
@JsonInclude
public class ExportPreVo implements Serializable {

    /**
     * 预入职ID
     */
    private Integer employment_id;
    /** 用户姓名 */
    private String user_name;
    /** 性别 */
    private String gender;

    /** 电话 */
    private String phone;

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

    /** 最高学历 */
    private String highest_degree;

    /** 毕业院校 */
    private String graduated_school;

    /** 毕业专业 */
    private String graduated_speciality;

    /** 最近工作单位 */
    private String last_work_company;

    /** 入职日期 */
    private Date hire_date;

    /** 试用期限(月) */
    private Integer probation_period;
    /** 应聘岗位*/
    private String application_position;
    /** 入职部门 */
    private Integer org_id;
    /**
     * 入职部门编码
     */
    private String org_code;
    /**
     * 部门名称
     */
    private String org_name;
    /** 入职岗位 */
    private Integer post_id;
    /**
     * 入职岗位编码
     */
    private String post_code;
    /**
     * 岗位名称
     */
    private String post_name;

    /** 备注 */
    private String description;
}
