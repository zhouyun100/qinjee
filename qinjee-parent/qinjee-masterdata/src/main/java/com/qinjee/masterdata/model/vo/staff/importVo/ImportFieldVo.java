package com.qinjee.masterdata.model.vo.staff.importVo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude
public class ImportFieldVo {
    private Boolean isSystemDefine;
    private String  tableName;
    private Integer tableId;
}
