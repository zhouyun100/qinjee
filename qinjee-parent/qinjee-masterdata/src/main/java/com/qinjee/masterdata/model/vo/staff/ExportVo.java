package com.qinjee.masterdata.model.vo.staff;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class ExportVo implements Serializable {
    @NotNull
    private String path;
    private String tittle;
    @NotNull
    private ArchiveShowVo archiveShowVo;
}
