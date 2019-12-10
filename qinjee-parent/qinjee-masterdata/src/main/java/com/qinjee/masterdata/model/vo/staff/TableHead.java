package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude
public class TableHead implements Serializable {
    private String name;
    private Integer index;
    private String  key;
    private Integer isShow;
}
