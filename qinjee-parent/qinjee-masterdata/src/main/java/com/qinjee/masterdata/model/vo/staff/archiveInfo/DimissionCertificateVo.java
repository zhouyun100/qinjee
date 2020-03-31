package com.qinjee.masterdata.model.vo.staff.archiveInfo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class DimissionCertificateVo {

    private String userName;

    private String gender;

    private String idNumber;

    private Date hireDate;

    private String postName;

    private Date attritionDate;


}
