package com.qinjee.masterdata.model.entity;

import java.io.Serializable;

/**
 * t_field_type
 * @author
 */
public class FieldType implements Serializable {
    /**
     * 类型code
     */
    private String typeCode;

    /**
     * 类型名称
     */
    private String typeName;

    private static final long serialVersionUID = 1L;

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
