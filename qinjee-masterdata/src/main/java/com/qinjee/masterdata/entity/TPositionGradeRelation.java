package com.qinjee.masterdata.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 职位职等关系表
 * @author
 */
@Data
@NoArgsConstructor
public class TPositionGradeRelation implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 职位ID
     */
    private Integer positionId;

    /**
     * 职等ID
     */
    private Integer positionGradeId;

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
