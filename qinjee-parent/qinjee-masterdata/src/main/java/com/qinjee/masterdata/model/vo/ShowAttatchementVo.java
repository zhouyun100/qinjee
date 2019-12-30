package com.qinjee.masterdata.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude
public class ShowAttatchementVo implements Serializable {
    private String group;
    private List <String> groupName;
}
