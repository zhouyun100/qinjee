package com.qinjee.masterdata.model.vo.staff.export;

import java.io.Serializable;
import java.util.Date;

public class ExportBlaVo implements Serializable {
    /**
     * 姓名
     */
    private String userName;
    /**
     * 性别
     */
    private String gender;
    /**
     * 证件号码
     */
    private String idNumber;
    /**
     * 联系电话
     */
    private String tel;
    /**
     * 单位名称
     */
    private Integer businessUnitName;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * 岗位名称
     */
    private String postName;
    /**
     * 拉黑原因
     */
    private String blockReason;
    /**
     * 拉黑时间
     */
    private Date blockTime;
    /**
     * 操作人
     */
    private Integer operator;


}
