package com.qinjee.masterdata.model.vo.staff.archiveInfo;

import com.qinjee.masterdata.model.vo.staff.PreEmploymentVo;
import com.qinjee.masterdata.model.vo.staff.archiveInfo.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 预入职登记信息
 */
@Data
public class PreRegistVo implements Serializable {

    /**
     * 预入职基本信息
     */
    private PreEmploymentVo preEmploymentVo;

    /**
     * 教育经历
     */
    private List<EducationExperienceVo> educationExperienceList;


    /**
     * 家庭成员及社会关系
     */
    private List<FamilyMemberAndSocialRelationsVo> familyMemberAndSocialRelationsList;


    /**
     * 工作经历
     */
    private List<WorkExperienceVo> workExperienceList;
}
