package com.qinjee.masterdata.model.vo.staff.export;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
@Data
@JsonInclude
public class BlackListVo implements Serializable {

    private String user_name;
    private String gender;
    private String id_number;
    private String phone;
    /**
     * 所属单位
     */
    private String business_unit_name;
    /**
     * 部门
     */
    private String org_name;
    /**
     * 岗位
     */
    private String post_name;
    /**
     * 拉黑原因
     */
    private String block_reason;
}
