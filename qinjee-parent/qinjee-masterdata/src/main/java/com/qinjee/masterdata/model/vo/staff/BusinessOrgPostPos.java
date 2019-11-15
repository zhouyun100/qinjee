package com.qinjee.masterdata.model.vo.staff;

import lombok.Data;

import java.io.Serializable;
/**
 * @author Administrator
 */
@Data
public class BusinessOrgPostPos implements Serializable {
   private Integer businessUnitId;
   private Integer orgId;
   private Integer postId;
   private Integer positionId;
}
