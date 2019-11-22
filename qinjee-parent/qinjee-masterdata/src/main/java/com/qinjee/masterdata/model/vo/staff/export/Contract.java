package com.qinjee.masterdata.model.vo.staff.export;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
@Data
@JsonInclude
public class Contract {
    private Integer id;
    private String user_name;
    private String id_number;
    private String employment_number;
    /**
     * 合同类别（这个数据库里面没有这个字段）
     */
    private String contract_category;
    private String contract_number;
    /**
     *期限类型
     */
    private String contract_period_type;
    private Date   contract_sign_date;
    private Date   contract_begin_date;
    private Date contract_end_date;
    /**
     *期限月
     */
    private Integer   contract_period_month;
    private String    contract_subject;
    private Integer   sign_number;
    /**
     *合同备注
     */
    private String    contract_remark;
}
