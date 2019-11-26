package com.qinjee.masterdata.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
@Data
@JsonInclude
public class SendMessageModel implements Serializable {
    @NotNull
    private List <Integer> list;
    @NotNull
    private Integer templateId;
    @NotNull
    private List<String> params;
}
