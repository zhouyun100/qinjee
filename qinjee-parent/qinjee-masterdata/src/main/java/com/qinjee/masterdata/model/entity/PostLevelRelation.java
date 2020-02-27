package com.qinjee.masterdata.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_post_level_relation
 * @author
 */
@Data
@JsonInclude
public class PostLevelRelation implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 职位ID
     */
    private Integer postId;

    /**
     * 职级ID
     */
    private Integer positionLevelId;

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
