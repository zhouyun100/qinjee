package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
@JsonInclude
@Data
public class DetailCount implements Serializable {
    private Integer preCount;
    private Integer conCount;
}
