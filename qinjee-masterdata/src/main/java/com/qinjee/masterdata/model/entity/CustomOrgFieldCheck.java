package com.qinjee.masterdata.model.entity;

import java.io.Serializable;

/**
 * t_custom_org_field_check
 * @author
 */
public class CustomOrgFieldCheck implements Serializable {
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
