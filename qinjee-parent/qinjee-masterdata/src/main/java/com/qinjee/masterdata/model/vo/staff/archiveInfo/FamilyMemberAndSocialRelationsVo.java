package com.qinjee.masterdata.model.vo.staff.archiveInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 家庭成员及社会关系
 */
@Data
public class FamilyMemberAndSocialRelationsVo implements Serializable {

    private String salutation; //关系
    private String name; //姓名
    private String birthDate; //出生日期
    private String positiionName; //职位名称
    private String phone; //联系电话
    private String businessUnitName; //单位名称
}
