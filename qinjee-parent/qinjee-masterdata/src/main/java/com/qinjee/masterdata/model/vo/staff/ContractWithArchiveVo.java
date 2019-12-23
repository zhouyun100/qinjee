package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@JsonInclude
public class ContractWithArchiveVo implements Serializable {
    private String orgName;
    private String bussinessUnitName;
    private String employeeNumber;
    private String idNumber;
    private String contractNumber;
    private Date   contractSignDate;
    private Date   contractBeginDate;
    private Date   contractEndDate;
    private String contractPeriodType;
    private Integer contractPeriodMonth;
    private Integer signNumber;
    private String  contractSubject;
    private Short   isEnable;
    private String  contractState;
}
