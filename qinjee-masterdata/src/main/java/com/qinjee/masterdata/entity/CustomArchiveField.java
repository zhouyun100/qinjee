package com.qinjee.masterdata.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_custom_archive_field
 * @author 
 */
public class CustomArchiveField implements Serializable {
    /**
     * 字段ID
     */
    private Integer fieldId;

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 字段类型
     */
    private String fieldType;

    /**
     * 验证类型
     */
    private String validType;

    /**
     * 企业代码ID
     */
    private Integer codeId;

    /**
     * 企业代码名称
     */
    private String codeName;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 长度
     */
    private Integer valLength;

    /**
     * 精度
     */
    private Integer valPrecision;

    /**
     * 表ID
     */
    private Integer tableId;

    /**
     * 组ID
     */
    private Integer groupId;

    /**
     * 是否系统定义
     */
    private Short isSystemDefine;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 操作人ID
     */
    private Integer creatorId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Short getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Short isDelete) {
        this.isDelete = isDelete;
    }
}