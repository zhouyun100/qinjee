package com.qinjee.masterdata.model.vo.organization.check;

import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import lombok.Data;

/**
 * @program: eTalent
 * @description:
 * @author: penghs
 * @create: 2019-11-30 20:24
 **/
@Data
public class OrganizationCheckVo extends OrganizationVO {

  /**
   * 校验是否成功(true:是，false:否)
   */
  private Boolean checkResult;

  /**
   * 校验结果信息
   */
  private StringBuilder resultMsg;

}
