package com.qinjee.masterdata.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_role_data_level_auth
 * @author 
 */
public class RoleDataLevelAuth implements Serializable {
    /**
     * ID
     */
    private Integer id;

    /**
     * 角色ID
     */
    private Integer roleId;

    /**
     * 左括号
     */
    private Short isLeftBrackets;

    /**
     * 字段ID
     */
    private Integer filedId;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Short getIsLeftBrackets() {
        return isLeftBrackets;
    }

    public void setIsLeftBrackets(Short isLeftBrackets) {
        this.isLeftBrackets = isLeftBrackets;
    }

    public Integer getFiledId() {
        return filedId;
    }

    public void setFiledId(Integer filedId) {
        this.filedId = filedId;
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