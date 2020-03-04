package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude
public class FieldValueForSearch {
    private String fieldName;
    private Object fieldValue;
    private String orderBy;
}
