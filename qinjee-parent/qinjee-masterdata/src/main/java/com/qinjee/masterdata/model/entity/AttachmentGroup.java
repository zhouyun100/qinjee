/*
 * Welcome to use the TableGo Tools.
 * 
 * http://vipbooks.iteye.com
 * http://blog.csdn.net/vipbooks
 * http://www.cnblogs.com/vipbooks
 * 
 * Author:bianj
 * Email:edinsker@163.com
 * Version:5.8.0
 */

package com.qinjee.masterdata.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Administrator
 */
@Data
@JsonInclude
public class AttachmentGroup implements Serializable {


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