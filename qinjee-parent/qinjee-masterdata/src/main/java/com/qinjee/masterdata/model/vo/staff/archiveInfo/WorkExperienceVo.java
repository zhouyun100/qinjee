package com.qinjee.masterdata.model.vo.staff.archiveInfo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 工作经历
 */
@Data
public class WorkExperienceVo implements Serializable {

    private Date startDate;//起始时间
    private Date endDate;//结束时间
    private String postName;//岗位名称
    private String businessUnitName;//单位名称
}
