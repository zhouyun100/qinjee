package com.qinjee.masterdata.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 职位表
 * @author
 */
@Data
@NoArgsConstructor
public class TPosition implements Serializable {
    /**
     * 职位ID
     */
    private Integer positionId;

    /**
     * 职位名称
     */
    private String positionName;

    /**
     * 职位族ID
     */
    private Integer positionGroupId;

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
