package com.qinjee.masterdata.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_custom_field
 * @author
 */
@Data
@JsonInclude
public class CustomField implements Serializable {
    private Integer fieldId;

    private String fieldName;

    private String fieldType;

    private String validType;

    private Integer codeId;

    private String codeName;

    private String defaultValue;

    private Integer valLength;

    private Integer valPrecision;

    private Integer tableId;

    private Integer groupId;

    private Short isSystemDefine;
    private Integer sort;
    private Date createTime;
    private Integer creatorId;
    private Date operatorTime;

    private Short isDelete;

    private String fieldCode;

    private static final long serialVersionUID = 1L;
}

