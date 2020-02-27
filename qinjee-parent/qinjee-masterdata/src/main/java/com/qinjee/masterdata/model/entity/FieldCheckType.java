package com.qinjee.masterdata.model.entity;

import java.io.Serializable;

/**
 * t_field_check_type
 * @author 
 */
public class FieldCheckType implements Serializable {
    private Integer id;

    private Integer fieldId;

    private String checkCode;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }
}