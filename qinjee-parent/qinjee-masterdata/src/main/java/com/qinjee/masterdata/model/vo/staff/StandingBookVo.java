package com.qinjee.masterdata.model.vo.staff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StandingBookVo implements Serializable {
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
     * 是否删除
     */
    private Short isDelete;

    private static final long serialVersionUID = 1L;

}
