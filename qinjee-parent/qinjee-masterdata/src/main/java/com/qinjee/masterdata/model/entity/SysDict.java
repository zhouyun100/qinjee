package com.qinjee.masterdata.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * SysDict
 * @author 周赟
 */
@Data
public class SysDict implements Serializable {
    /**
     * ID
     */
    private Integer id;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 字典CODE
     */
    private String dictCode;

    /**
     * 字典VALUE
     */
    private String dictValue;

    /**
     * 描述
     */
    private String description;

    /**
     * 操作人ID
     */
    private Integer operatorId;

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