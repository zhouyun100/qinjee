package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
@ToString
@JsonInclude
public class StandingBookFilterVo implements Serializable {
    /**
     * 筛选ID
     */
    private Integer filterId;

    /**
     * 左括号
     */
    private Short isLeftBrackets;

    /**
     * 字段ID
     */
    @NotNull
    private Integer fieldId;

    /**
     * 操作符
     */
    @NotNull
    private String operateSymbol;

    /**
     * 字段值
     */
    @NotNull
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
     * 是否删除
     */
    private Short isDelete;
    private static final long serialVersionUID = 1L;
}
