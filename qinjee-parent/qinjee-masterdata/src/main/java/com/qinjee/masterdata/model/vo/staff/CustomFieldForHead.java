package com.qinjee.masterdata.model.vo.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qinjee.masterdata.model.entity.SysDict;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@JsonInclude
@Data
public class CustomFieldForHead implements Serializable {
    private String name;
    private String key;
    private String type;
    private String inputType;
    private Short isShow;
    private List<SysDict> dictList;
    private String textType;
    private String code;
}
