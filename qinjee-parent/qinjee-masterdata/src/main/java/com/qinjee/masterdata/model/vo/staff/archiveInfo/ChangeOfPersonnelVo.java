package com.qinjee.masterdata.model.vo.staff.archiveInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 人事变动信息
 */
@Data
public class ChangeOfPersonnelVo implements Serializable {
    @CustomFieldMapAnno("")
    private String laborDate;//劳动合同时间

    @CustomFieldMapAnno("")
    private String changeType;//变动类型

    @CustomFieldMapAnno("")
    private String afterChangeBusinessUnitName;//变动后单位名称

    @CustomFieldMapAnno("")
    private String afterChangeOrgName;//变动后部门名称

    @CustomFieldMapAnno("")
    private String afterChangePostName;//变动后岗位名称

}
