package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
@ToString
@JsonInclude
public class LaborContractVo implements Serializable {
    /**
     * contractId
     */
    private Integer contractId;

    /**
     * 合同签订日期
     */
    @NotNull
    private String contractSignDate;

    /**
     * 合同开始日期
     */
    @NotNull
    private String contractBeginDate;

    /**
     * 合同结束日期
     */
    @NotNull
    private String contractEndDate;

    /**
     * 合同期限(月)
     */
    private Integer contractPeriodMonth;

    /**
     * 合同期限类型
     */
    @NotNull
    private String contractPeriodType;

    /**
     * 合同主体
     */
    private String contractSubject;

    /**
     * 合同编号
     */
    @NotNull
    private String contractNumber;

    /**
     * 合同备注
     */
    private String contractRemark;

    /**
     * 签订次数
     */
    @NotNull
    private Integer signNumber;

    /**
     * 新签人员id
     */
    private Integer archiveId;

    private static final long serialVersionUID = 1L;

}
