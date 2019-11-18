package com.qinjee.masterdata.model.vo.staff.export;

import com.qinjee.masterdata.model.vo.staff.ArchiveShowVo;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
/**
 * @author Administrator
 */
@Data
public class ExportFile implements Serializable {
    private String tittle;
    @NotNull
    private ArchiveShowVo archiveShowVo;

}
