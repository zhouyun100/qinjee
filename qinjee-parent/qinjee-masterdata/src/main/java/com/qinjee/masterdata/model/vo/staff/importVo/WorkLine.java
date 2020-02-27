package com.qinjee.masterdata.model.vo.staff.importVo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
@JsonInclude
public class WorkLine implements Serializable {
    private String user_name;
    private String gender;
    private Date work_begin_date;
    private Date work_end_date;
    /**
     * 所在单位
     */
    private String org_name;
    /**
     * 所在岗位
     */
    private String post_name;
    /**
     * 证明人姓名
     */
    private String confirm_name;
    /**
     * 变动原因
     */
    private String change_reson;
}
