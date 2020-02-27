package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude
public class QuerySchemaVo implements Serializable {
    private Integer archiveId;
    private Integer querySchemeId;
    private String  querySchemeName;
    private Integer sort;
    private List <Integer>  fieldId;
    private List<Sort> sorts;

}
