package com.qinjee.masterdata.model.vo.staff.archiveInfo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 解除劳动合同证明书信息查询  ，用于报表打印
 */
@Data
@Builder
public class RelieveContractInfoVo {

    private String userName;

    private String idNumber;
    private String hireDate;

    private String lastContractStartDate;//最近一次合同开始时间
    private String lastContractEndDate;//最近一次合同结束时间
    private String postName;

    private String reason;//解除原因

    private Date contractEndDate;//解除合同时间


}
