package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@ToString
@JsonInclude
public class StatusChangeVo {
    /**
     * 预入职id
     */
    @NotNull
    private List<Integer> preEmploymentList;
    /**
     * 变更状态
     */
    @NotNull
    private String changeState;
    /**
     * 延期入职时间
     *
     */
    private Date delayTime;

    /**
     * 放弃原因
     */
    @NotNull
    private String abandonReason;

    /**
     * 变更描述
     */
    private String changeRemark;

}
