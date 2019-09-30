package com.qinjee.masterdata.model.vo.staff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
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
     * 是否删除
     */
    private Short isDelete;
    private static final long serialVersionUID = 1L;
}
