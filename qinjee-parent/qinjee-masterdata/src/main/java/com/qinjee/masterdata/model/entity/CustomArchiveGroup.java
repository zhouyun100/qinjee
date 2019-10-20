package com.qinjee.masterdata.model.entity;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * t_custom_archive_group
 * @author
 */
@Data
@ToString
public class CustomArchiveGroup implements Serializable {
    /**
     * 组ID
     */
    private Integer groupId;

    /**
     * 组名称
     */
    @NotNull
    private String groupName;

    /**
     * 表ID
     */
    @NotNull
    private Integer tableId;

    /**
     * 排序
     */
    @NotNull
    private Integer sort;

    /**
     * 操作人ID
     */
    private Integer creatorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Short isDelete;

    private static final long serialVersionUID = 1L;

}
