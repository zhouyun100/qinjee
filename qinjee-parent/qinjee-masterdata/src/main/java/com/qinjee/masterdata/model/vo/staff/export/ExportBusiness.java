package com.qinjee.masterdata.model.vo.staff.export;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class ExportBusiness implements Serializable {
    private String tittle;
    private List< Map<String,String> > list;
}
