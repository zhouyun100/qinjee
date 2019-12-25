package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

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
    private String changeType;

    /**
     * 变更日期
     */
    private String changeDate;

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
