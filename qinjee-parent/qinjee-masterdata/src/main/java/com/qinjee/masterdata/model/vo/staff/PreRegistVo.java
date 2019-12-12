package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
@JsonInclude
public class PreRegistVo implements Serializable {
    private List <Integer> list ;
    private Integer templateId;
    private Integer sendWay;
}
