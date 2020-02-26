package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
/**
 * @author Administrator
 */
@Data
@JsonInclude

public class ArchiveCareerTrackVo implements Serializable {
    /** 变更ID */
    private Integer changeId;

    /** 档案ID */
    @NotNull
    private Integer archiveId;

    /** 人员分类 */
    private String userCategory;
    /** 单位 */
    @NotNull
    private Integer businessUnitId;
    /** 单位 */

    private String businessUnitName;
    /** 部门 */
    @NotNull
    private Integer orgId;
    /** 部门 */
    private String orgName;
    /** 岗位 */
    @NotNull
    private Integer postId;
    /** 岗位 */
    private String postName;
    /** 职位 */
    private Integer positionId;
    /** 职位 */
    private String positionName;

    /** 变更类型 */
    @NotNull
    private String changeType;

    /** 变更日期 */

    private Date changeDate;

    /** 变更原因 */
    private String changeReason;

}
