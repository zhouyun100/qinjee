package com.qinjee.masterdata.model.vo.staff.archiveInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 家庭成员及社会关系
 */
@Data
public class FamilyMemberAndSocialRelationsVo implements Serializable {


    @CustomFieldMapAnno("salutation")
    private String salutation; //关系

    @CustomFieldMapAnno("user_name")
    private String name; //姓名

    @CustomFieldMapAnno("birth_date")
    private String birthDate; //出生日期

    @CustomFieldMapAnno("positiion_name")
    private String positiionName; //职位名称

    @CustomFieldMapAnno("phone")
    private String phone; //联系电话

    @CustomFieldMapAnno("business_unit_name")
    private String businessUnitName; //单位名称
}
