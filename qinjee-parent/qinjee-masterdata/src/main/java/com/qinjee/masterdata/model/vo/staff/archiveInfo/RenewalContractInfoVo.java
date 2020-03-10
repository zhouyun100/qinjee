package com.qinjee.masterdata.model.vo.staff.archiveInfo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
/**
 * 续签通知书信息查询  ，用于报表打印
 */
@Data
@Builder
public class RenewalContractInfoVo {
    private String userName;
    private String gender;
    private String idNumber;
    private Date contractEndDate;
}
