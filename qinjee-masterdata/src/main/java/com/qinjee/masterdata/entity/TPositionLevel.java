package com.qinjee.masterdata.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 职级表
 * @author
 */
@Data
@NoArgsConstructor
public class TPositionLevel implements Serializable {
    /**
     * 职级ID
     */
    private Integer positionLevelId;

    /**
     * 职级名称
     */
    private String positionLevelName;

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
