package com.qinjee.masterdata.model.vo.staff.export;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

/**
 * @author Administrator
 */
@Data
@JsonInclude
public class ExportFile implements Serializable {
    private String tittle;
    @NotNull
    private Map<Integer, Map <String,Object> > map;
}
