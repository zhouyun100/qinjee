package com.qinjee.masterdata.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_user_org_auth
 * @author
 */
public class UserOrgAuth implements Serializable {
    /**
     * 权限ID
     */
    private Integer authId;

    /**
     * 机构ID
     */
    private Integer orgId;

    /**
     * 档案ID
     */
    private Integer archiveId;

    /**
     * 操作人
     */
    private Integer operatorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除
     */
    private Short isDelete;

    private static final long serialVersionUID = 1L;

    public Integer getAuthId() {
        return authId;
    }

    public void setAuthId(Integer authId) {
        this.authId = authId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(Integer archiveId) {
        this.archiveId = archiveId;
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

    public Short getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Short isDelete) {
        this.isDelete = isDelete;
    }
}
