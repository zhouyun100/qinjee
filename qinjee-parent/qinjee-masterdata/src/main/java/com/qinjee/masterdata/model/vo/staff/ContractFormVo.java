package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
@Data
@JsonInclude
public class ContractFormVo implements Serializable {
    private String  businessUnitName;
    private String  orgName;
    private Integer  orgId;
    private Integer staffCount;
    private Integer signCount;
    private Integer noSignCount;
    private Integer delineCount;
}
