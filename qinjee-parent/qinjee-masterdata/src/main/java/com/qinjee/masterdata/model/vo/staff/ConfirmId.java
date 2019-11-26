package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
@Data
@JsonInclude
public class ConfirmId implements Serializable {
    @NotNull
    private List <Integer> list;
    @NotNull
    private Integer ruleId;
}
