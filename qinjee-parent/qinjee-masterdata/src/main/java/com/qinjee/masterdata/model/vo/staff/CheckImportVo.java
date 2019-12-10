package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qinjee.masterdata.model.vo.custom.CheckCustomTableVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
@JsonInclude
public class CheckImportVo implements Serializable {

    private List < CheckCustomTableVO > list;

    private List<TableHead> headList;
}
