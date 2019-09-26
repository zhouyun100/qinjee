package com.qinjee.masterdata.model.vo.staff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BlackListVo implements Serializable {


    /**
     * 姓名
     */
    private String userName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 证件类型
     */
    private String idType;

    /**
     * 证件号码
     */
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
