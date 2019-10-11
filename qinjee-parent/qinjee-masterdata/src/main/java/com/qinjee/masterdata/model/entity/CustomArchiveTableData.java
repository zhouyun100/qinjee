package com.qinjee.masterdata.model.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * t_custom_archive_table_data
 * @author
 */
@Data
@ToString
public class CustomArchiveTableData implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 表ID
     */
    private Integer tableId;

    /**
     * 业务ID
     */
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
    private String bigData;

    private static final long serialVersionUID = 1L;

}
