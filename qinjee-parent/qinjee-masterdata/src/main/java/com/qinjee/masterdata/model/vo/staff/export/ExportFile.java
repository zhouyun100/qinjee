package com.qinjee.masterdata.model.vo.staff.export;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qinjee.masterdata.model.vo.staff.ExportList;
import lombok.Data;

import java.io.Serializable;
/**
 * @author Administrator
 */
@Data
@JsonInclude
public class ExportFile implements Serializable {
    private String tittle;
    private ExportList exportList;

}
