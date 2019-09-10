package com.qinjee.masterdata.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 职等表
 * @author
 */
@Data
@NoArgsConstructor
public class TPositionGrade implements Serializable {
    /**
     * 职等ID
     */
    private Integer positionGradeId;

    /**
     * 职等名称
     */
    private String positionGradeName;

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
