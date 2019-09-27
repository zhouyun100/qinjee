package com.qinjee.masterdata.model.vo.staff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LaborContractVo implements Serializable {
    private Integer probationPeriod;

    /**
     * 合同签订日期
     */
    private Date contractSignDate;

    /**
     * 合同开始日期
     */
    private Date contractBeginDate;

    /**
     * 合同结束日期
     */
    private Date contractEndDate;

    /**
     * 合同期限(月)
     */
    private Integer contractPeriodMonth;

    /**
     * 合同期限类型
     */
    private String contractPeriodType;

    /**
     * 合同主体
     */
    private String contractSubject;

    /**
     * 合同编号
     */
    private String contractNumber;

    /**
     * 合同备注
     */
    private String contractRemark;

    /**
     * 签订次数
     */
    private Integer signNumber;


    private static final long serialVersionUID = 1L;

}
