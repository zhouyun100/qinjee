package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
@JsonInclude
public class ExportReadyConVo implements Serializable {
   private List<ContractWithArchiveVo> list;
   private List<Integer> orgIdList;
   private List<String> status;
   private List<FieldValueForSearch> searchList;
}
