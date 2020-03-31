package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qinjee.masterdata.model.entity.ContractRenewalIntention;
import lombok.Data;
import java.io.Serializable;

@Data
@JsonInclude
public class RenewIntionAboutUser extends ContractRenewalIntention implements Serializable {

    /**
     * 用户名
     */
    private String userName;
    /**
     * 证件号
     */
    private  String idNumber;
}
