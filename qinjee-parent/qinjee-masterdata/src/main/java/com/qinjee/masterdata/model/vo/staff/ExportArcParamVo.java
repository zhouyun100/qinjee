package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
@JsonInclude
public class ExportArcParamVo implements Serializable {
    private Integer querySchemaId;
    List<Integer> list;
    List<Integer> orgIdList;
}
