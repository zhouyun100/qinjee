package com.qinjee.masterdata.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_custom_field
 * @author 
 */
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

    private static final long serialVersionUID = 1L;

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getValidType() {
        return validType;
    }

    public void setValidType(String validType) {
        this.validType = validType;
    }

    public Integer getCodeId() {
        return codeId;
    }

    public void setCodeId(Integer codeId) {
        this.codeId = codeId;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer getValLength() {
        return valLength;
    }

    public void setValLength(Integer valLength) {
        this.valLength = valLength;
    }

    public Integer getValPrecision() {
        return valPrecision;
    }

    public void setValPrecision(Integer valPrecision) {
        this.valPrecision = valPrecision;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Short getIsSystemDefine() {
        return isSystemDefine;
    }

    public void setIsSystemDefine(Short isSystemDefine) {
        this.isSystemDefine = isSystemDefine;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Date getOperatorTime() {
        return operatorTime;
    }

    public void setOperatorTime(Date operatorTime) {
        this.operatorTime = operatorTime;
    }

    public Short getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Short isDelete) {
        this.isDelete = isDelete;
    }
}