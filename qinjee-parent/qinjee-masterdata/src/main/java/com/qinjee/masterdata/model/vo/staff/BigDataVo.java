package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@JsonInclude
@Data
public class BigDataVo implements Serializable {
    @NotNull
    private String jsonString;
    @NotNull
    private String title;
    @NotNull
    private Integer businessId;
}
