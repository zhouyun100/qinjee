package com.qinjee.masterdata.model.vo.staff;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ReportRegulationCountBO {
    private List<Integer> orgIds;
    private Date startDate;
    private Date endDate;
    private Integer layer;
}
