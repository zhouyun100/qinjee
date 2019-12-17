package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
@JsonInclude
public class ContractVo implements Serializable {
   private LaborContractVo laborContractVo;
   private List <Integer> list;
   private LaborContractChangeVo laborContractChangeVo;
}
