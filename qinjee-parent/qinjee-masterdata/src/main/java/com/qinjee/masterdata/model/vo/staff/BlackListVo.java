package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
@ToString
@JsonInclude
public class BlackListVo implements Serializable {

    /**
     * 姓名
     */
    @NotNull
    private String userName;

    /**
     * 手机号
     */
    @NotNull
    private String phone;

    /**
     * 证件类型
     */
    @NotNull
    private String idType;

    /**
     * 证件号码
     */
    @NotNull
    private String idNumber;

    /**
     * 所属单位ID
     */
    private String businessUnitName;

    /**
     * 部门ID
     */
    private String orgName;

    /**
     * 岗位ID
     */
    private String postName;

    /**
     * 拉黑原因
     */
    private String blockReason;
    /**
     * 数据来源
     */
    private  String dataSource;
}
