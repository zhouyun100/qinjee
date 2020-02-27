package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude
public class MoblieCustom implements Serializable {
    private List<Integer> tableIdList;
    private List<CustomArchiveTableDataVo> list;
}
