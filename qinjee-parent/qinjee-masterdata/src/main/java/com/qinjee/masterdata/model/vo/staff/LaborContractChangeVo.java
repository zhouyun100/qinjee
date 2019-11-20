package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
@ToString
@JsonInclude
public class LaborContractChangeVo implements Serializable {
    /**
     * id
     */
    private Integer id;
    /**
     * 合同ID
     */
    private Integer contractId;

    /**
     * 变更类型
     */
    @NotNull
    private String changeType;

    /**
     * 变更日期
     */
    @NotNull
    private Date changeDate;

    /**
     * 变更原因
     */
    @NotNull
    private String changeReason;

    /**
     * 补偿金额
     */
    private BigDecimal compensateAmount;

    /**
     * 备注
     */
    private String remark;

}
