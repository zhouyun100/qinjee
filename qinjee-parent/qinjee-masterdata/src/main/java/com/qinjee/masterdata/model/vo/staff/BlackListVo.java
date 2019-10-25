package com.qinjee.masterdata.model.vo.staff;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
@ToString
public class BlackListVo implements Serializable {


    /**
     * id
     */
    private Integer id;

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
    private Integer businessUnitId;

    /**
     * 部门ID
     */
    private Integer orgId;

    /**
     * 岗位ID
     */
    private Integer postId;

    /**
     * 拉黑原因
     */
    private String blockReason;
}
