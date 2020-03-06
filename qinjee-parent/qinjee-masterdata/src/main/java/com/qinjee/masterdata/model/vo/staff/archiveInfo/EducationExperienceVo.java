package com.qinjee.masterdata.model.vo.staff.archiveInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 教育经历
 */
@Data
public class EducationExperienceVo implements Serializable {

    @CustomFieldMapAnno("start_school_date")
    private String startSchoolDate;//入学时间

    @CustomFieldMapAnno("graduate_date")
    private String graduateDate;//毕业时间

    @CustomFieldMapAnno("degree")
    private String degree;//学历

    @CustomFieldMapAnno("graduate_school")
    private String graduateSchool;//毕业院校

    @CustomFieldMapAnno("major")
    private String major;//专业

    @CustomFieldMapAnno("certificate")
    private String certificate;//学位

}
