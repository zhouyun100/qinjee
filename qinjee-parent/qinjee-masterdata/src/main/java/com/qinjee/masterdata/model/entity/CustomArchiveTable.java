package com.qinjee.masterdata.model.entity;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * t_custom_archive_table
 * @author
 */
@Data
@ToString
public class CustomArchiveTable implements Serializable {
    /**
     * 表ID
     */
    private Integer tableId;
    /**
     * 表名
     */
    @NotNull
    private String tableName;
    /**
     * 物理表名
     */
    @NotNull
    private String tableCode;
    /**
     * 功能code
     */
    @NotNull
    private String funcCode;

    /**
     * 企业ID
     */
    @NotNull
    private Integer companyId;

    /**
     * 是否系统定义
     */
    @NotNull
    private Short isSystemDefine;

    /**
     * 是否启用
     */
    @NotNull
    private Short isEnable;

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
    @NotNull
    private Short isDelete;

    private static final long serialVersionUID = 1L;

}
