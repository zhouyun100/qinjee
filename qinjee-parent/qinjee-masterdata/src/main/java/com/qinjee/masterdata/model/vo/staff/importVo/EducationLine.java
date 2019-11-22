package com.qinjee.masterdata.model.vo.staff.importVo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
@JsonInclude
public class EducationLine implements Serializable {
    private String user_name;
    private String id_number;
    private String employment_number;
    /**
     *入学时间
     */
    private Date intake_date;
    /**
     *毕业时间
     */
    private Date graduate_date;
    /**
     *毕业院校
     */
    private String graduate_college;
    /**
     *学历
     */
    private String school_record;
    /**
     *专业
     */
    private String specialities;
    /**
     *学习形式
     */
    private String study_form;
    /**
     * 学位
     */
    private String degree;
    /**
     * 是否最高学历
     */
    private Short is_highest_record;

}
