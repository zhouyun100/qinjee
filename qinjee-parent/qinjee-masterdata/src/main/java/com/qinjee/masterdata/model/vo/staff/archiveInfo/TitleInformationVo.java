package com.qinjee.masterdata.model.vo.staff.archiveInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 职称信息
 */
@Data
public class TitleInformationVo implements Serializable {

    @CustomFieldMapAnno("title")
    private String title;//职称名称

    @CustomFieldMapAnno("title_level")
    private String titleLevel;//职称等级

    @CustomFieldMapAnno("get_title_date")
    private String getTheDate;//获得日期
}
