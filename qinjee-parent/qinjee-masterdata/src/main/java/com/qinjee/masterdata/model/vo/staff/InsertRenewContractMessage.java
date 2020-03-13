package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
@Data
@JsonInclude
public class InsertRenewContractMessage implements Serializable {
    private String message;
    private Integer id;
    private Short isAgree;
}
