package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude
public class FieldValueForSearch {
    private String fieldName;
    private List fieldValue;
    private String orderBy;
    private String fieldType;
}
