package com.qinjee.masterdata.model.vo.staff.archiveInfo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 教育经历
 */
@Data
public class EducationExperienceVo implements Serializable {

    private Date startSchoolDate;//入学时间
    private Date graduateDate;//毕业时间
    private String educationBackground;//学历
    private String graduateSchool;//毕业院校
    private String major;//专业
    private String degree;//学位

}
