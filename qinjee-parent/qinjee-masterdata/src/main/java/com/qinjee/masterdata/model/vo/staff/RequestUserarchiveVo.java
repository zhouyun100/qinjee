package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
@JsonInclude
public class RequestUserarchiveVo implements Serializable {
  private List <Integer> orgId ;
  private Integer pageSize;
  private Integer currentPage;
  private Integer querySchemaId;
}
