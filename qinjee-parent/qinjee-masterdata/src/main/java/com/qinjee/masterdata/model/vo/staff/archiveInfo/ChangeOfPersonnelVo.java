package com.qinjee.masterdata.model.vo.staff.archiveInfo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 人事变动信息
 */
@Data
public class ChangeOfPersonnelVo implements Serializable {
    private Date laborDate;//劳动合同时间
    private String changeType;//变动类型
    private String afterChangeBusinessUnitName;//变动后单位名称
    private String afterChangeOrgName;//变动后部门名称
    private String afterChangePostName;//变动后岗位名称

}
