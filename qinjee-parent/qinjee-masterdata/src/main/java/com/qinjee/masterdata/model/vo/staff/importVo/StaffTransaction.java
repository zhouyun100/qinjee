package com.qinjee.masterdata.model.vo.staff.importVo;

import java.io.Serializable;
import java.util.Date;

public class StaffTransaction implements Serializable {
    private String user_name;
    private String employment_number;
    /**
     *变动前人员分类
     */
    private String _before;
    private String org_name_before;
    private String org_number_before;
    private String org_number_after;
    private String org_name_after;
    private String post_number_before;
    private String post_name_before;
    private String post_number_after;
    private String post_name_after;
    private String position_before;
    private String position_after;
    /**
     *变动原因
     */
    private String staffTransaction_change_reason;
    /**
     * 变动时间
     */
    private Date staffTransaction_change_date;

}
