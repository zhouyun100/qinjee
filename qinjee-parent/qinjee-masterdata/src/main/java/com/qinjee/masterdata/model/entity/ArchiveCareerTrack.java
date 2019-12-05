package com.qinjee.masterdata.model.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author Administrator
 */
@Data
public class ArchiveCareerTrack {
    /** 版本号 */
    private static final long serialVersionUID = 3896584191643968254L;

    /** 变更ID */
    private Integer changeId;

    /** 档案ID */
    private Integer archiveId;

    /** 变更前人员分类 */
    private String beforeUserCategory;

    /** 变更后人员分类 */
    private String afterUserCategory;

    /** 变更前单位 */
    private Integer beforeBusinessUnitId;

    /** 变更后单位 */
    private Integer afterBusinessUnitId;

    /** 变更前部门 */
    private Integer beforeOrgId;

    /** 变更后部门 */
    private Integer afterOrgId;

    /** 变更前岗位 */
    private Integer beforePostId;

    /** 变更后岗位 */
    private Integer afterPostId;

    /** 变更前职位 */
    private Integer beforePositionId;

    /** 变更后职位 */
    private Integer afterPositionId;

    /** 变更类型 */
    private String changeType;

    /** 变更日期 */
    private Date changeDate;

    /** 变更原因 */
    private String changeReason;

    /** 操作人ID */
    private Integer operatorId;

    /** 创建时间 */
    private Date createTime;
}
