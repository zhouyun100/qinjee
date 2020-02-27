package com.qinjee.masterdata.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * t_check_type
 * @author
 */
@JsonInclude
@Data
public class CheckType implements Serializable {
    /**
     * 验证code
     */
    private String checkCode;

    /**
     * 验证名称
     */
    private String checkName;

    private static final long serialVersionUID = 1L;

}
