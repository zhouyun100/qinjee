package com.qinjee.masterdata.model.entity;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * t_custom_archive_field
 * @author
 */
@Data
@ToString
public class CustomArchiveField implements Serializable {
    /**
     * 字段ID
     */
    private Integer fieldId;
    /**
     * 物理字段名
     */
    @NotNull
    private String fieldCode;
    /**
     * 字段名
     */
    @NotNull
    private String fieldName;

    /**
     * 字段类型
     */
    @NotNull
    private String fieldType;

    /**
     * 企业代码ID
     */
    private Integer codeId;

    /**
     * 企业代码名称
     */
    private String codeName;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 长度
     */
    private Integer valLength;

    /**
     * 精度
     */
    private Integer valPrecision;

    /**
     * 表ID
     */
    @NotNull
    private Integer tableId;

    /**
     * 组ID
     */
    private Integer groupId;

    /**
     * 是否系统定义
     */
    @NotNull
    private Short isSystemDefine;

    /**
     * 排序
     */
    @NotNull
    private Integer sort;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 操作人ID
     */
    private Integer creatorId;

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
