package com.qinjee.masterdata.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_user_org_auth
 * @author
 */
@Data
@JsonInclude
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
     * 角色ID
     */
    private Integer roleId;

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
}
