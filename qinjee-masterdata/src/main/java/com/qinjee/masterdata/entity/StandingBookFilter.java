package com.qinjee.masterdata.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_standing_book_filter
 * @author 
 */
public class StandingBookFilter implements Serializable {
    /**
     * 筛选ID
     */
    private Integer filterId;

    /**
     * 台账ID
     */
    private Integer standingBookId;

    /**
     * 左括号
     */
    private Short isLeftBrackets;

    /**
     * 字段ID
     */
    private Integer fieldId;

    /**
     * 操作符
     */
    private String operateSymbol;

    /**
     * 字段值
     */
    private String fieldValue;

    /**
     * 右括号
     */
    private Short isRightBrackets;

    /**
     * 连接符
     */
    private String linkSymbol;

    /**
     * SQL拼接串
     */
    private String sqlStr;

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

    public Integer getFilterId() {
        return filterId;
    }

    public void setFilterId(Integer filterId) {
        this.filterId = filterId;
    }

    public Integer getStandingBookId() {
        return standingBookId;
    }

    public void setStandingBookId(Integer standingBookId) {
        this.standingBookId = standingBookId;
    }

    public Short getIsLeftBrackets() {
        return isLeftBrackets;
    }

    public void setIsLeftBrackets(Short isLeftBrackets) {
        this.isLeftBrackets = isLeftBrackets;
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public String getOperateSymbol() {
        return operateSymbol;
    }

    public void setOperateSymbol(String operateSymbol) {
        this.operateSymbol = operateSymbol;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public Short getIsRightBrackets() {
        return isRightBrackets;
    }

    public void setIsRightBrackets(Short isRightBrackets) {
        this.isRightBrackets = isRightBrackets;
    }

    public String getLinkSymbol() {
        return linkSymbol;
    }

    public void setLinkSymbol(String linkSymbol) {
        this.linkSymbol = linkSymbol;
    }

    public String getSqlStr() {
        return sqlStr;
    }

    public void setSqlStr(String sqlStr) {
        this.sqlStr = sqlStr;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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