package com.qinjee.masterdata.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * t_custom_archive_table_data
 * @author
 */
@Data
@ToString
@JsonInclude
public class CustomArchiveTableData implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 表ID
     */
    @NotNull
    private Integer tableId;

    /**
     * 业务ID
     */
    @NotNull
    private Integer businessId;

    /**
     * 操作人ID
     */
    private Integer operatorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 操作时间
     */
    private Date updateTime;

    /**
     * 数据大字段
     */
    @NotNull
    private String bigData;

    /**
     * 是否删除
     */
    private  Integer isDelete;
    private static final long serialVersionUID = 1L;

}
