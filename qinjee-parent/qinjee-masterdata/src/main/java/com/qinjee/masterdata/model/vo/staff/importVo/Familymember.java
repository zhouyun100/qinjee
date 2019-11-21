package com.qinjee.masterdata.model.vo.staff.importVo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
@JsonInclude
public class Familymember implements Serializable {
    private String user_name;
    private String id_number;
    private String employment_number;
    /**
     *成员姓名
     */
    private String member_name;
    /**
     * 与本人关系
     */
    private String relation;
    private Date birth_date;
    /**
     *成员电话
     */
    private String member_phone;
    /**
     *成员工作电话
     */
    private String member_org_name;
    /**
     *成员职位
     */
    private String member_position;
}
