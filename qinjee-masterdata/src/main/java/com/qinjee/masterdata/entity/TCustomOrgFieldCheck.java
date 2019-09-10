package com.qinjee.masterdata.entity;

import java.io.Serializable;

/**
 * 数据中心字段验证类型
 * @author
 */
public class TCustomOrgFieldCheck implements Serializable {
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
