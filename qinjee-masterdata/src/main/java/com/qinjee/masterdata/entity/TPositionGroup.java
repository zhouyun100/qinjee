package com.qinjee.masterdata.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 职位族表
 * @author
 */
@Data
@NoArgsConstructor
public class TPositionGroup implements Serializable {
    /**
     * 职位族ID
     */
    private Integer positionGroupId;

    /**
     * 职位族名称
     */
    private String positionGroupName;

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
     * 是否删除
     */
    private Short isDelete;

    private static final long serialVersionUID = 1L;


}
