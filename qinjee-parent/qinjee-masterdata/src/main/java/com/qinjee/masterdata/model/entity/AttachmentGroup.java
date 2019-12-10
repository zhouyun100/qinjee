package com.qinjee.masterdata.model.entity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * 附件组表(t_attachment_group)
 * 
 * @author 周赟
 * @version 2019-12-10
 */
@Data
public class AttachmentGroup implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 7343668129572052862L;

    /** 组ID */
    private Integer groupId;

    /** 组名称 */
    private String groupName;

    /** 组类型 */
    private String groupType;

    /** 父级组ID */
    private Integer parentGroupId;

    /** 附件数量 */
    private Integer attachmentNumber;

    /** 操作人ID */
    private Integer operatorId;

    /** 创建时间 */
    private Timestamp createTime;

    /** 更新时间 */
    private Timestamp updateTime;

    /** 是否删除 */
    private Integer isDelete;

}