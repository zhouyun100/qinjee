package com.qinjee.masterdata.model.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author Administrator
 */
@Data
public class ArchiveCareerTrack {
    /** 版本号 */
    private static final long serialVersionUID = 1L;

    /** 变更ID */
    private Integer changeId;

    /** 档案ID */
    private Integer archiveId;

    /** 人员分类 */
    private String userCategory;

    /** 单位 */
    private Integer businessUnitId;

    /** 部门 */
    private Integer orgId;

    /** 岗位 */
    private Integer postId;

    /** 职位 */
    private Integer positionId;

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
