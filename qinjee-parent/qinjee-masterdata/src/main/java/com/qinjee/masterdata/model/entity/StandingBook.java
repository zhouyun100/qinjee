package com.qinjee.masterdata.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_standing_book
 * @author
 */
@JsonInclude
@Data
public class StandingBook implements Serializable {
    /**
     * 台账ID
     */
    private Integer standingBookId;

    /**
     * 台账名称
     */
    private String standingBookName;

    /**
     * 台账备注
     */
    private String standingBookRemark;

    /**
     * 台账描述
     */
    private String standingBookDescribe;

    /**
     * 企业ID
     */
    private Integer companyId;

    /**
     * 档案ID
     */
    private Integer archiveId;

    /**
     * 是否共享
     */
    private Short isShare;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否启用
     */
    private Short isEnable;

    /**
     * 操作人ID
     */
    private Integer creatorId;

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
