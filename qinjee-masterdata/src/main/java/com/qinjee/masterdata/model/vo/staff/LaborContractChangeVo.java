package com.qinjee.masterdata.model.vo.staff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LaborContractChangeVo implements Serializable {
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
    private Date changeDate;

    /**
     * 变更原因
     */
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
