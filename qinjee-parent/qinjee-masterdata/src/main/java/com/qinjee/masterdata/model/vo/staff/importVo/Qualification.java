package com.qinjee.masterdata.model.vo.staff.importVo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@JsonInclude
@Data
public class Qualification implements Serializable {
  private String user_name;
  private String id_number;
  private String employment_number;
  /**
   * 资格名称
   */
  private String qualification_name;
  /**
   *证书号
   */
  private String qualification_number;
  /**
   *职业工种
   */
  private String occupational_work;
  /**
   *发证单位
   */
  private String qualification_org_name;
  /**
   *发证日期
   */
  private Date qualification_date;


}
