package com.qinjee.masterdata.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_standing_book_filter
 * @author
 */
@Data
@JsonInclude
public class StandingBookFilter implements Serializable {
    /**
     * 筛选ID
     */
    private Integer filterId;

    /**
     * 台账ID
     */
    private Integer standingBookId;

    /**
     * 左括号
     */
    private Short isLeftBrackets;

    /**
     * 字段ID
     */
    private Integer fieldId;

    /**
     * 操作符
     */
    private String operateSymbol;

    /**
     * 字段值
     */
    private String fieldValue;

    /**
     * 右括号
     */
    private Short isRightBrackets;

    /**
     * 连接符
     */
    private String linkSymbol;

    /**
     * SQL拼接串
     */
    private String sqlStr;

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
