package com.qinjee.masterdata.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_company_code
 * @author 
 */
public class CompanyCode implements Serializable {
    /**
     * 代码ID
     */
    private Integer codeId;

    /**
     * 代码名称
     */
    private String codeName;

    /**
     * 代码父级ID
     */
    private Integer codeParentId;

    /**
     * 是否系统定义
     */
    private Short isSystemDefine;

    /**
     * 企业ID
     */
    private Integer companyId;

    /**
     * 排序ID
     */
    private Integer sortId;

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

    public Integer getCodeParentId() {
        return codeParentId;
    }

    public void setCodeParentId(Integer codeParentId) {
        this.codeParentId = codeParentId;
    }

    public Short getIsSystemDefine() {
        return isSystemDefine;
    }

    public void setIsSystemDefine(Short isSystemDefine) {
        this.isSystemDefine = isSystemDefine;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getSortId() {
        return sortId;
    }

    public void setSortId(Integer sortId) {
        this.sortId = sortId;
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