package com.qinjee.masterdata.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * t_custom_archive_table
 * @author
 */
@Data
@ToString
@JsonInclude
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
    @DateTimeFormat(pattern = "yyyy-MM-dd" )//页面写入数据库时格式化
    @JSONField(format = "yyyy-MM-dd ")//数据库导出页面时json格式化
    private Date createTime;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd" )//页面写入数据库时格式化
    @JSONField(format = "yyyy-MM-dd ")//数据库导出页面时json格式化
    private Date updateTime;

    /**
     * 是否删除
     */
    private Short isDelete;

    private static final long serialVersionUID = 1L;

}
