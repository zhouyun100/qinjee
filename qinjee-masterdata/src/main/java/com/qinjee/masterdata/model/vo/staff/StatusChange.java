package com.qinjee.masterdata.model.vo.staff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author Administrator
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StatusChange {

    /**
     * 预入职ID
     */
    private Integer employmentId;

    /**
     * 变更状态
     */
    private String changeState;
    /**
     * 延期入职时间
     */
    private Date DelayTime;

    /**
     * 放弃原因
     */
    private String abandonReason;

    /**
     * 变更描述
     */
    private String changeRemark;

    /**
     * 操作人ID
     */
    private Integer operatorId;
}
