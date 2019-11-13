package com.qinjee.masterdata.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/**
 * @author Administrator
 */
@Data
public class ArchiveCareerTrack implements Serializable {
    /**
     * 变更id
     */
    private Integer changeId;
    /**
     *档案id
     */
    private Integer archiveId;
    /**
     *变更类型
     */
    private String  changType;
    /**
     *生效日期
     */
    private Date    effectiveDate;
    /**
     *部门id
     */
    private Integer orgId;
    /**
     *岗位id
     */
    private Integer postId;
    /**
     *操作人
     */
    private Integer operatorId;
    /**
     *创建时间
     */
    private Date    createTime;
    private static final long serialVersionUID = 1L;
}
