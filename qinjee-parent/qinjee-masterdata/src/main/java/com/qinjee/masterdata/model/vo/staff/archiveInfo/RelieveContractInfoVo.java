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

    private String contractBeginDate;//最近一次合同开始时间
    private String contractEndDate;//最近一次合同结束时间
    private String postName;

    //字典型
    private String changeReason;//解除原因

    private Date changeDate;//解除合同时间


}
