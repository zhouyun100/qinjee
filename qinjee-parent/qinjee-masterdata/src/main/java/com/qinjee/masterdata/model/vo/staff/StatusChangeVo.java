package com.qinjee.masterdata.model.vo.staff;

import lombok.*;

import java.util.Date;

/**
 * @author Administrator
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class StatusChangeVo {
    /**
     * id
     */
    private Integer id;
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

}
