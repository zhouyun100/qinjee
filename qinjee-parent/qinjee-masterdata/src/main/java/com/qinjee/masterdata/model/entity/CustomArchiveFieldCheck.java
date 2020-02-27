package com.qinjee.masterdata.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * t_custom_archive_field_check
 * @author
 */
@JsonInclude
@Data
public class CustomArchiveFieldCheck implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 字段ID
     */
    private Integer fieldId;

    /**
     * 验证code
     */
    private String checkCode;

    private static final long serialVersionUID = 1L;

}
