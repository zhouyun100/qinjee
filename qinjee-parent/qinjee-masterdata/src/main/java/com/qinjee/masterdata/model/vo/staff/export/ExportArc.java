package com.qinjee.masterdata.model.vo.staff.export;

import com.qinjee.masterdata.model.vo.staff.ArchiveShowVo;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class ExportArc implements Serializable {
    @NotNull
    private String path;
    private String tittle;
    @NotNull
    private ArchiveShowVo archiveShowVo;

}
