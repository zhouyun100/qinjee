package com.qinjee.masterdata.model.vo.staff;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
@Data
public class ExportVo implements Serializable {
    @NotNull
    private String path;
    private String tittle;
    private  Integer querySchemeId;
    @NotNull
    private List<Integer> list;

}
