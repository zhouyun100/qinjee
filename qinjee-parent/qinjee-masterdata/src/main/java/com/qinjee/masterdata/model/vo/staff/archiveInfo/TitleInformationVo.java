package com.qinjee.masterdata.model.vo.staff.archiveInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 职称信息
 */
@Data
public class TitleInformationVo implements Serializable {

    private String titleName;//职称名称
    private String titleLevel;//职称等级
    private String getTheDate;//获得日期
}
