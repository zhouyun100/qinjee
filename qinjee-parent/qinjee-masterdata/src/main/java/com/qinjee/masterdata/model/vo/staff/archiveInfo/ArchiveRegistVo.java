package com.qinjee.masterdata.model.vo.staff.archiveInfo;

import com.qinjee.masterdata.model.vo.staff.PreEmploymentVo;
import com.qinjee.masterdata.model.vo.staff.UserArchiveVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 员工登记信息vo
 */
@Data
public class ArchiveRegistVo implements Serializable {

    /**
     * 档案基本信息
     */
    private UserArchiveVo userArchiveVo;

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

    /**
     * 职称信息
     */
    private List<TitleInformationVo> titleInformationList;

    /**
     * 人事变动
     */
    private List<ChangeOfPersonnelVo> changeOfPersonnelList;


}
