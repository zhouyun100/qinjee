package com.qinjee.masterdata.model.vo.staff.export;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
@Data
@ToString
public class ExportConVo implements Serializable {
    /**
     * 工号
     */
    @NotNull
    private String employeeNumber;
    /**
     * 姓名
     */
    @NotNull
    private String userName;
    /**
     * 性别
     */
    @NotNull
    private String gender;
    /**
     * 机构编码
     */
    private String orgCode;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * 岗位编码
     */
     private String postCode;
    /**
     * 岗位名称
     */
    private String postName;
    /**
     * 合同期限类型
     */
    private String contractPeriodType;
    /**
     * 合同签订日期
     */
    private Date contractSignDate;
    /**
     * 合同开始日期
     */
    private Date contractBeginDate;

    /**
     * 合同结束日期
     */
    private Date contractEndDate;
    /**
     * 合同期限(月)
     */
    private Integer contractPeriodMonth;
    /**
     * 签订次数
     */
    private Integer signNumber;
    /**
     * 合同主体
     */
    private String contractSubject;

}
