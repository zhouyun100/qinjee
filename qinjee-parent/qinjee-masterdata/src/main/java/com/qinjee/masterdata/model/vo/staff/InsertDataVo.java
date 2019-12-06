package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude
public class InsertDataVo implements Serializable {
    @NotNull
    private String funcCode;
    @NotNull
    private List < Map < Integer, String > > list;
}
