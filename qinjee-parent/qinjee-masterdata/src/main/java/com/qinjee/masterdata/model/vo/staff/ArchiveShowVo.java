package com.qinjee.masterdata.model.vo.staff;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;
@Data
public class ArchiveShowVo implements Serializable {
    private Integer querySchemaId;
    @NotNull
    private Map<Integer,Map<String,Object>> map;
}
