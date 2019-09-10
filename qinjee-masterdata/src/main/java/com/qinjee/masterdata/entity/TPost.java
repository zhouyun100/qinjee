package com.qinjee.masterdata.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 企业岗位表
 * @author
 */
@Data
@NoArgsConstructor
public class TPost implements Serializable {
    /**
     * 岗位ID
     */
    private Integer postId;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 岗位编码
     */
    private String postCode;

    /**
     * 父级岗位
     */
    private Integer parentPostId;

    /**
     * 机构ID
     */
    private Integer orgId;

    /**
     * 企业ID
     */
    private Integer companyId;

    /**
     * 职位ID
     */
    private Integer positionId;

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

}
