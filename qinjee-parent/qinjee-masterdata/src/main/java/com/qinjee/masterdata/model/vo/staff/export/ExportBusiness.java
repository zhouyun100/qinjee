package com.qinjee.masterdata.model.vo.staff.export;

import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.util.List;
@Data
public class ExportBusiness implements Serializable {
    private String path;
    private String tittle;
    private List<T> list;
}
