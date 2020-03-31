package com.qinjee.masterdata.model.vo.staff.archiveInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 工作经历
 */
@Data
public class WorkExperienceVo implements Serializable {

    @CustomFieldMapAnno("start_date")
    private String startDate;//起始时间

    @CustomFieldMapAnno("end_date")
    private String endDate;//结束时间

    @CustomFieldMapAnno("post_name")
    private String postName;//岗位名称

    @CustomFieldMapAnno("business_unit_name")
    private String businessUnitName;//单位名称
}
