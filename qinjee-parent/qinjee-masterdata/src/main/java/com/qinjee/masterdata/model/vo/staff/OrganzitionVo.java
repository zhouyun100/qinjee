package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
@JsonInclude
public class OrganzitionVo implements Serializable {
    private Integer org_id;
    private Integer org_parent_id;
    private String org_name;
    private List<OrganzitionVo> list;
}
