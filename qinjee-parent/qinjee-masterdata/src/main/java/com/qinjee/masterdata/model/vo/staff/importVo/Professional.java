package com.qinjee.masterdata.model.vo.staff.importVo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
@Data
@JsonInclude
public class Professional implements Serializable {
    private String user_name;
    private String id_number;
    private String employment_number;
    /**
     * 职称资格
     */
    private String professional_title;
    /**
     * 职称资格等级
     */
    private String professional_rank;
    /**
     * 职称证编号
     */
    private String professional_number;
    /**
     * 职称批准单位
     */
    private String professional_org_name;
    /**
     * 获得资格途径
     */
    private String get_professional_way;

}
